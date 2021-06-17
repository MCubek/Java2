package hr.fer.zemris.java.webserver;

import hr.fer.oprpp1.custom.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * My very smart http web server.
 * Server can server static and dynamic web content.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 30/03/2021
 */
@SuppressWarnings("FieldCanBeLocal")
public class SmartHttpServer {
    private final String address;
    private final String domainName;
    private final int port;
    private final int workerThreads;
    private final int sessionTimeout;
    private final Map<String, String> mimeTypes = new HashMap<>();
    private final Map<String, IWebWorker> workersMap = new HashMap<>();
    private final ServerThread serverThread;
    private ExecutorService threadPool;
    private final Path documentRoot;

    private final Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();
    private final Random sessionRandom = new Random();

    private final Path configRoot = Path.of("config");
    private final CookieCleaner cookieCleaner = new CookieCleaner();


    /**
     * Server constructor that configures http server.
     * Server takes config file name that it looks for inside config directory.
     *
     * @param configFileName name of config file in config directory
     * @throws IOException if error occurs while reading configs
     */
    public SmartHttpServer(String configFileName) throws IOException {
        Path propertiesFile = configRoot.resolve(configFileName);
        if (! Files.isReadable(propertiesFile)) throw new IllegalArgumentException("Config file inaccessible!");

        Properties properties = new Properties();
        properties.load(Files.newInputStream(propertiesFile));

        address = properties.getProperty("server.address");
        domainName = properties.getProperty("server.domainName");
        port = Integer.parseInt(properties.getProperty("server.port"));
        workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
        sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
        documentRoot = Path.of(properties.getProperty("server.documentRoot"));

        configureMimeTypesFromConfig(Path.of(properties.getProperty("server.mimeConfig")));
        configureWorkersFromConfig(Path.of(properties.getProperty("server.workers")));

        serverThread = new ServerThread();
    }

    /**
     * @param mimeConfig path of mime config file
     * @throws IOException if error while reading config
     */
    private void configureMimeTypesFromConfig(Path mimeConfig) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(mimeConfig));

        for (var property : properties.keySet()) {
            mimeTypes.put(property.toString(), properties.getProperty(property.toString()));
        }
    }

    /**
     * @param workersConfig path of worker config file
     * @throws IOException if error while reading config
     */
    private void configureWorkersFromConfig(Path workersConfig) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(workersConfig));

        properties.forEach((k, v) -> {
            String path = k.toString();
            String fqcn = v.toString();

            try {
                workersMap.put(path, loadWorker(fqcn));
            } catch (Exception e) {
                System.out.println("Error while assigning worker " + fqcn);
            }
        });
    }

    /**
     * Method gets class from its fully qualified class name.
     *
     * @param fqcn Fully qualified class name
     * @return {@link IWebWorker} object from class
     * @throws Exception when error occurs
     */
    private IWebWorker loadWorker(String fqcn) throws Exception {
        Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
        Object obj = referenceToClass.getDeclaredConstructor().newInstance();
        return (IWebWorker) obj;
    }

    /**
     * Starts the server.
     */
    protected synchronized void start() {
        threadPool = Executors.newFixedThreadPool(workerThreads);
        if (! serverThread.isAlive())
            serverThread.start();

        if (! cookieCleaner.isAlive()) {
            cookieCleaner.setDaemon(true);
            cookieCleaner.start();
        }
    }

    /**
     * Stops the server.
     */
    protected synchronized void stop() {
        if (! serverThread.isInterrupted())
            serverThread.interrupt();

        if (! cookieCleaner.isInterrupted())
            cookieCleaner.interrupt();

        threadPool.shutdown();
    }

    /**
     * Thread that listens for new requests and submits jobs of responses and request handling
     * to Threadpool.
     */
    protected class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(address, port));
                while (! Thread.interrupted()) {
                    Socket client = serverSocket.accept();
                    ClientWorker clientWorker = new ClientWorker(client);
                    threadPool.submit(clientWorker);
                }
            } catch (IOException e) {
                System.out.println("Server starting error.");
            }
        }
    }

    /**
     * Thread that periodically cleans expired sessions.
     */
    protected class CookieCleaner extends Thread {
        @Override
        public void run() {
            while (! isInterrupted()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000 * 60 * 5);
                    //ConcurentHashMap doesnt throw concurent modification exception
                    for (String sid : sessions.keySet()) {
                        if (sessions.get(sid).validUntil < new Date().getTime())
                            sessions.remove(sid);
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    /**
     * Job that will be executed on ThreadPool that handles client talking and response.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private class ClientWorker implements Runnable, IDispatcher {
        private final Socket clientSocket;
        private InputStream inputStream;
        private OutputStream outputStream;
        private String version;
        private String method;
        private String host;
        private final Map<String, String> params = new HashMap<>();
        private final Map<String, String> tempParams = new HashMap<>();
        private Map<String, String> permParams;
        private final List<RequestContext.RCCookie> outputCookies = new ArrayList<>();
        private String SID;

        private RequestContext context;

        public ClientWorker(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                inputStream = clientSocket.getInputStream();
                outputStream = clientSocket.getOutputStream();
            } catch (IOException e) {
                return;
            }

            //Parse http request
            List<String> request;
            try {
                var requestString = new String(readRequest(inputStream).orElseThrow(IllegalArgumentException::new), StandardCharsets.US_ASCII);
                request = extractHeaders(requestString);
            } catch (IOException | IllegalArgumentException e) {
                return;
            }
            // If header is invalid (less then a line at least) return response status 400
            if (request.size() < 1) {
                sendSimpleMessage(400, "Bad request.");
                return;
            }

            // Extract (method, requestedPath, version) from firstLine
            // if method not GET or version not HTTP/1.0 or HTTP/1.1 return response status 400
            String firstLine = request.get(0);
            String[] firstLineArray = firstLine.split(" ");
            method = firstLineArray[0];
            if (! method.equalsIgnoreCase("GET")) {
                sendSimpleMessage(400, "Only GET Method allowed.");
                return;
            }
            version = firstLineArray[2];
            if (! (version.equalsIgnoreCase("HTTP/1.1") || version.equalsIgnoreCase("HTTP/1.0"))) {
                sendSimpleMessage(400, "Only HTTP/1.0 and HTTP/1.1 supported.");
                return;
            }

            String requestedPath = firstLineArray[1];

            // Go through headers, and if there is header “Host: xxx”, assign host property
            // to trimmed value after “Host:”; else, set it to server’s domainName
            // If xxx is of form some-name:number, just remember “some-name”-part
            var optionalHost = request.stream()
                    .filter(str -> str.toUpperCase().startsWith("HOST:"))
                    .map(str -> str.substring("HOST:".length()).trim())
                    .map(str -> str.split(":")[0])
                    .findFirst();
            host = optionalHost.orElse(domainName);

            fetchOrCreateSession(request);

            // Parse parameters
            String path;
            String paramString;

            var split = requestedPath.split("[?]");
            path = split[0];
            if (split.length == 2) {
                paramString = split[1];
                parseParameters(paramString);
            }

            try {
                internalDispatchRequest(path, true);
            } catch (Exception e) {
                sendSimpleMessage(500, "Internal server error.");
                System.out.println("ERROR:");
                System.out.println(e.getMessage());
            }

            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error while closing socket!");
            }
        }

        private void fetchOrCreateSession(List<String> requestList) {
            var cookiesRequestMap = requestList.stream()
                    .filter(str -> str.startsWith("Cookie:"))
                    .map(str -> str.replace("Cookie:", ""))
                    .flatMap(str -> Arrays.stream(str.split(";")))
                    .map(String::trim)
                    .collect(Collectors.toMap(str -> str.split("=")[0],
                            str -> {
                                String val = str.split("=")[1];
                                return val.substring(1, val.length() - 1);
                            }));

            SID = cookiesRequestMap.getOrDefault("sid", generateSid());
            long time = new Date().getTime();

            synchronized (sessions) {
                SessionMapEntry sessionMapEntry = sessions.get(SID);

                if (sessionMapEntry == null || ! sessionMapEntry.host.equals(this.host) || sessionMapEntry.validUntil < time) {
                    sessionMapEntry = new SessionMapEntry(SID, host, time + sessionTimeout * 1000L, new ConcurrentHashMap<>());
                    sessions.put(SID, sessionMapEntry);
                }

                outputCookies.add(new RequestContext.RCCookie("sid", SID, null, host, "/"));
                sessionMapEntry.validUntil = time + sessionTimeout * 1000L;

                permParams = sessionMapEntry.map;
            }
        }

        synchronized private String generateSid() {
            StringBuilder sb = new StringBuilder(20);
            int firstUppercaseIndex = 'A';
            for (int i = 0; i < 20; i++) {
                int letterIndex = sessionRandom.nextInt(26);
                char randomUppercase = (char) (firstUppercaseIndex + letterIndex);
                sb.append(randomUppercase);
            }
            return sb.toString();
        }

        @Override
        public void dispatchRequest(String urlPath) throws Exception {
            internalDispatchRequest(urlPath, false);
        }

        private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
            if (context == null)
                context = new RequestContext(outputStream, params, permParams, outputCookies, tempParams, this, SID);

            if (directCall && urlPath.startsWith("/private")) {
                sendSimpleMessage(404, "Not Found.");
                return;
            }

            if (urlPath.equals("/")) urlPath = "/index2.html";

            String workerExtension = "/ext/";
            String workerDefaultPath = "hr.fer.zemris.java.webserver.workers.";

            if (urlPath.startsWith(workerExtension)) {
                try {
                    var worker = loadWorker(workerDefaultPath + urlPath.substring(workerExtension.length()));
                    worker.processRequest(context);
                    outputStream.flush();
                    return;
                } catch (Exception e) {
                    sendSimpleMessage(404, "Worker " + urlPath.substring(workerExtension.length()) + " not found.");
                    return;
                }
            }

            IWebWorker worker = workersMap.get(urlPath);
            if (worker != null) {
                worker.processRequest(context);
                outputStream.flush();
                return;
            }

            Path requestedFile = documentRoot.toAbsolutePath().resolve(urlPath.substring(1));
            if (! requestedFile.toAbsolutePath().toString().startsWith(documentRoot.toAbsolutePath().toString())) {
                sendSimpleMessage(403, "Forbidden.");
                return;
            }
            if (! Files.isReadable(requestedFile)) {
                sendSimpleMessage(404, "Not Found.");
                return;
            }

            String filename = requestedFile.getFileName().toString();
            String extension = filename.substring(filename.lastIndexOf(".") + 1);

            // create a context = new RequestContext(...); set mime-type; set status to 200
            context.setStatusCode(200);
            context.setStatusText("OK");

            if (extension.equals("smscr")) {
                //Request for script
                String document = Files.readString(requestedFile);
                new SmartScriptEngine((DocumentNode) new SmartScriptParser(document).getHeadNode(), context).execute();

            } else {
                //Request for Static content

                String mimeType = mimeTypes.getOrDefault(extension, "octet-stream");                context.setMimeType(mimeType);
                context.setContentLength(Files.size(requestedFile));

                byte[] buffer = new byte[1024];
                try (InputStream is = Files.newInputStream(requestedFile)) {
                    while (true) {
                        int len = is.read(buffer);
                        if (len < 1) break;
                        context.write(buffer, 0, len);
                    }
                }

            }
            outputStream.flush();
        }

        private void sendSimpleMessage(int statusCode, String statusText) {
            RequestContext rc = new RequestContext(outputStream, params, permParams, outputCookies);
            rc.setStatusCode(statusCode);
            rc.setStatusText(statusText);

            String message = "";

            if (statusCode == 404) {
                //noinspection HtmlUnknownTarget
                message = """
                        <img src="/images/404.jpg" alt="404" width='100%'>
                        """;
            }
            rc.setContentLength((long) message.length());

            try {
                rc.write(message);
                outputStream.flush();
            } catch (IOException ignore) {
            }
        }

        private void parseParameters(String parameters) {
            Arrays.stream(parameters.split("&"))
                    .forEach(param -> {
                        var split = param.split("=");
                        var value = split.length == 2 ? split[1] : "0";
                        params.put(split[0], value);
                    });
        }

        /**
         * Helper method for reading http headers into byte array.
         *
         * @param is from where headers are read
         * @return optional of bytes in which headers are parese
         * @throws IOException if error occurs when parsing
         */
        private static Optional<byte[]> readRequest(InputStream is) throws IOException {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int state = 0;
            l:
            while (true) {
                int b = is.read();
                if (b == - 1) {
                    if (bos.size() != 0) {
                        throw new IOException("Incomplete header received.");
                    }
                    return Optional.empty();
                }
                if (b != 13) {
                    bos.write(b);
                }
                switch (state) {
                    case 0:
                        if (b == 13) {
                            state = 1;
                        } else if (b == 10) state = 4;
                        break;
                    case 1:
                        if (b == 10) {
                            state = 2;
                        } else state = 0;
                        break;
                    case 2:
                        if (b == 13) {
                            state = 3;
                        } else state = 0;
                        break;
                    case 3:
                    case 4:
                        if (b == 10) {
                            break l;
                        } else state = 0;
                        break;
                }
            }
            return Optional.of(bos.toByteArray());
        }

        /**
         * Helper method for extracting headers from string.
         *
         * @param requestHeader http headers as string
         * @return list of headers
         */
        private static List<String> extractHeaders(String requestHeader) {
            List<String> headers = new ArrayList<>();
            StringBuilder currentLine = null;
            for (String s : requestHeader.split("\n")) {
                if (s.isEmpty()) break;
                char c = s.charAt(0);
                if (c == 9 || c == 32) {
                    assert currentLine != null;
                    currentLine.append(s);
                } else {
                    if (currentLine != null) {
                        headers.add(currentLine.toString());
                    }
                    currentLine = new StringBuilder(s);
                }
            }
            assert currentLine != null;
            if (currentLine.length() > 0) {
                headers.add(currentLine.toString());
            }
            return headers;
        }

    }

    static private class SessionMapEntry {
        String sid;
        String host;
        long validUntil;
        Map<String, String> map;

        public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
            this.sid = sid;
            this.host = host;
            this.validUntil = validUntil;
            this.map = map;
        }
    }

    public static void main(String[] args) {
        try {
            var server = new SmartHttpServer("server.properties");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
