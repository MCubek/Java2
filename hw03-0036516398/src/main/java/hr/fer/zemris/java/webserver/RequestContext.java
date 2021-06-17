package hr.fer.zemris.java.webserver;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * HttpServers request context.
 * Used for generating server messages with all http jazz
 * and in client's favourite file type.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 27/03/2021
 */
@SuppressWarnings("unused")
public class RequestContext {
    private final OutputStream outputStream;
    @SuppressWarnings("FieldCanBeLocal")
    private Charset charset;

    private String encoding = "UTF-8";
    private int statusCode = 200;
    private String statusText = "OK";
    private String mimeType = "text/html";
    private Long contentLength = null;
    private Map<String, String> parameters;
    private Map<String, String> temporaryParameters;
    private Map<String, String> persistentParameters;
    private List<RCCookie> outputCookies;
    private boolean headerGenerated = false;
    private final IDispatcher dispatcher;
    private final String sid;

    public RequestContext(OutputStream outputStream,
                          Map<String, String> parameters,
                          Map<String, String> persistentParameters,
                          List<RCCookie> outputCookies,
                          Map<String, String> temporaryParameters,
                          IDispatcher dispatcher,
                          String sid) {
        this.outputStream = Objects.requireNonNull(outputStream);
        this.parameters = parameters;
        this.persistentParameters = persistentParameters;
        this.outputCookies = outputCookies;
        this.temporaryParameters = temporaryParameters;

        if (persistentParameters == null)
            this.persistentParameters = new HashMap<>();

        if (parameters == null)
            this.parameters = new HashMap<>();

        if (outputCookies == null)
            this.outputCookies = new ArrayList<>();

        if (temporaryParameters == null)
            this.temporaryParameters = new HashMap<>();

        this.dispatcher = dispatcher;
        this.sid = sid;
    }

    public RequestContext(OutputStream outputStream,
                          Map<String, String> parameters,
                          Map<String, String> persistentParameters,
                          List<RCCookie> outputCookies) {
        this(outputStream, parameters, persistentParameters, outputCookies, null, null, null);
    }

    public void setEncoding(String encoding) throws RuntimeException {
        if (headerGenerated)
            throw new RuntimeException();
        this.encoding = encoding;
    }

    public void setStatusCode(int statusCode) throws RuntimeException {
        if (headerGenerated)
            throw new RuntimeException();
        this.statusCode = statusCode;
    }

    public void setStatusText(String statusText) throws RuntimeException {
        if (headerGenerated)
            throw new RuntimeException();
        this.statusText = statusText;
    }

    public void setMimeType(String mimeType) throws RuntimeException {
        if (headerGenerated)
            throw new RuntimeException();
        this.mimeType = mimeType;
    }

    public void setContentLength(Long contentLength) throws RuntimeException {
        if (headerGenerated)
            throw new RuntimeException();
        this.contentLength = contentLength;
    }

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Method that retrieves value from parameters map (or null if no association exists)
     *
     * @param name parameter name
     * @return parameter value
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * Method that retrieves names of all parameters in parameters map.
     * Set is read only
     *
     * @return set with names of parameters
     */
    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    /**
     * Method that retrieves value from persistent parameters map (or null if no association exists)
     *
     * @param name parameter name
     * @return parameter value
     */
    public String getPersistentParameter(String name) {
        return persistentParameters.get(name);
    }

    /**
     * Method that retrieves names of all parameters in persistent parameters map.
     * Set is read only
     *
     * @return set with names of parameters
     */
    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    /**
     * Method that stores a value to persistentParameters map.
     *
     * @param name  key
     * @param value value
     */
    public void setPersistentParameter(String name, String value) {
        persistentParameters.put(name, value);
    }

    /**
     * Method that removes a value from persistentParameters map.
     *
     * @param name key to be removed
     */
    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    /**
     * Method that retrieves value from temporaryParameters map (or null if no association exists).
     *
     * @param name key of value
     * @return parameter value
     */
    public String getTemporaryParameter(String name) {
        return temporaryParameters.get(name);
    }

    /**
     * Method that retrieves names of all parameters in temporary parameters map.
     * Set is read only
     *
     * @return set with names of parameters
     */
    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    /**
     * Method that retrieves an identifier which is unique for current user session
     *
     * @return session id.
     */
    public String getSessionId() {
        return sid;
    }

    /**
     * Adds a cookie.
     *
     * @param cookie cookie to add
     */
    public void addRCCookie(RCCookie cookie) {
        outputCookies.add(cookie);
    }

    /**
     * Method that stores a value to temporaryParameters map.
     *
     * @param name  parameter name
     * @param value parameter value
     */
    public void setTemporaryParameter(String name, String value) {
        temporaryParameters.put(name, value);
    }

    /**
     * Method that removes a value from temporaryParameters map.
     *
     * @param name parameter name
     */
    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    @SuppressWarnings("UnusedReturnValue")
    public RequestContext write(String text) throws IOException {
        return write(text.getBytes(Charset.forName(encoding)));
    }

    public RequestContext write(byte[] data) throws IOException {
        return write(data, 0, data.length);
    }

    public RequestContext write(byte[] data, int offset, int len) throws IOException {
        if (! headerGenerated)
            outputStream.write(createHeader());

        outputStream.write(data, offset, len);

        //TODO Return this?
        return this;
    }

    private byte[] createHeader() throws IOException {
        charset = Charset.forName(encoding);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(convertStringToBytes(String.format("HTTP/1.1 %s %s\r\n", statusCode, statusText)));

        String completeMimeType = mimeType;
        if (completeMimeType.startsWith("text/"))
            completeMimeType += "; charset=" + encoding;
        bos.write(convertStringToBytes(String.format("Content-Type: %s\r\n", completeMimeType)));

        if (contentLength != null)
            bos.write(convertStringToBytes(String.format("Content-Length: %s\r\n", contentLength)));

        if (! outputCookies.isEmpty()) {
            StringBuilder cookies = new StringBuilder();

            for (RCCookie cookie : outputCookies) {
                cookies.append(String.format("Set-Cookie: %s=\"%s\"", cookie.name, cookie.value));

                if (cookie.domain != null)
                    cookies.append(String.format("; Domain=%s", cookie.domain));

                if (cookie.path != null)
                    cookies.append(String.format("; Path=%s", cookie.path));

                if (cookie.maxAge != null)
                    cookies.append(String.format("; Max-Age=%s", cookie.maxAge));

                if (cookie.name.equals("sid")) cookies.append("; HttpOnly");

                cookies.append("\r\n");
            }

            bos.write(convertStringToBytes(cookies.toString()));
        }

        bos.write(convertStringToBytes("\r\n"));

        headerGenerated = true;
        return bos.toByteArray();
    }

    private byte[] convertStringToBytes(String string) {
        return string.getBytes(StandardCharsets.US_ASCII);
    }


    public record RCCookie(String name, String value, Integer maxAge, String domain,
                           String path) {

    }
}

