package hr.fer.zemris.java.hw2.server;

import hr.fer.zemris.java.hw2.message.*;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Server in chit-chat app with all your best friends.
 * Server uses UDP datagrams to connect to all clients and distributes messages.
 * Don't forget to keep track of time when enjoying yourself with this amazing application.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 20/03/2021
 */
public class ChatServer {
    private final DatagramSocket socket;
    private final long port;

    //Uid is selected as random long on startup and gives each new client a incremented value
    private long currentUid = new Random().nextLong();

    //Key is random long and value is client UID
    private final Map<Long, Long> clientNumUidMap = new HashMap<>();
    //Key is UID and value is client
    private final Map<Long, Client> clients = new ConcurrentHashMap<>();

    public ChatServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
        this.port = port;
    }

    /**
     * Starts server listening
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void start() {
        System.out.println("Server started on port " + port + ".");

        while (true) {
            byte[] responseData = new byte[300];
            DatagramPacket response = new DatagramPacket(responseData, responseData.length);

            //Try to receive a response
            try {
                socket.receive(response);
            } catch (SocketTimeoutException timeoutException) {
                continue;
            } catch (IOException e) {
                System.out.println("Receive error: " + e.getMessage());
                continue;
            }

            Message receivedMessage;
            try {
                receivedMessage = MessageUtil.deserializeMessage(response.getData());
            } catch (IOException e) {
                System.out.println("Message deserialization error!");
                continue;
            }

            try {
                switch (receivedMessage.getMessageType()) {
                    case HELLO -> helloMessageHandler(receivedMessage, response);
                    case ACK -> acknowledgeMessageHandler(receivedMessage);
                    case BYE -> byeMessageHandler(receivedMessage);
                    case OUTMSG -> outboundMessageHandler(receivedMessage);
                    case INMSG -> {
                    }
                }
            } catch (IOException ioException) {
                System.out.println("Message handling error: " + ioException.getMessage());
            }

        }
    }

    private void outboundMessageHandler(Message receivedMessage) throws IOException {
        OutMessage outMessage = (OutMessage) receivedMessage;

        Client client = clients.get(outMessage.getUid());

        //No such current client
        if (client == null) {
            System.out.format("Client %d not found.", outMessage.getUid());
            return;
        }

        //Wrong message id
        if (outMessage.getMessageId() > client.getClientMessageNumber()) {
            System.out.format("Invalid message id for client %s.", client.getName());
            return;
        }

        //Right message id
        if (outMessage.getMessageId() == client.getClientMessageNumber()) {
            client.clientMessageNumber++;

            for (BlockingQueue<OutMessage> blockingQueue : clients.values().stream()
                    .map(cl -> cl.outMessages)
                    .collect(Collectors.toList())) {

                while (true) {
                    try {
                        blockingQueue.put(outMessage);
                        break;
                    } catch (InterruptedException ignored) {
                    }
                }
            }

        }


        //Send back acknowledgement for old registered messages or new ones
        AcknowledgeMessage acknowledgeMessage = new AcknowledgeMessage(receivedMessage.getMessageId(), client.getUid());
        byte[] sendData = MessageUtil.serializeMessage(acknowledgeMessage);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
        sendPacket.setSocketAddress(client.getAddress());
        socket.send(sendPacket);

    }

    private void byeMessageHandler(Message receivedMessage) throws IOException {
        ByeMessage byeMessage = (ByeMessage) receivedMessage;

        Client client = clients.get(byeMessage.getUid());

        //No such current client
        if (client == null) return;

        //Wrong message id
        if (byeMessage.getMessageId() != client.getClientMessageNumber()) return;


        //Remove client from server data
        clientNumUidMap.remove(client.getRandomNumber());
        clients.remove(client.getUid());
        client.clientHandler.interrupt();

        //Send back acknowledgement
        AcknowledgeMessage acknowledgeMessage = new AcknowledgeMessage(receivedMessage.getMessageId(), client.getUid());
        byte[] sendData = MessageUtil.serializeMessage(acknowledgeMessage);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
        sendPacket.setSocketAddress(client.getAddress());
        socket.send(sendPacket);
    }

    private void acknowledgeMessageHandler(Message receivedMessage) {
        AcknowledgeMessage acknowledgeMessage = (AcknowledgeMessage) receivedMessage;

        Client client = clients.get(acknowledgeMessage.getUid());

        //No such current client
        if (client == null) return;

        //Add message to clients queue
        while (true) {
            try {
                client.acknowledgeMessages.put(acknowledgeMessage);
                break;
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void helloMessageHandler(Message receivedMessage, DatagramPacket packet) throws IOException {
        HelloMessage helloMessage = (HelloMessage) receivedMessage;

        long clientNumber = helloMessage.getRandomKey();

        Client client;
        if (! clientNumUidMap.containsKey(clientNumber)) {
            //Create all necessary client structure
            client = new Client(helloMessage.getRandomKey(),
                    currentUid++,
                    helloMessage.getUsername(),
                    packet.getSocketAddress(),
                    1L,
                    1L);

            Thread clientHandler = new Thread(new ClientHandler(client,
                    clients,
                    socket));
            client.clientHandler = clientHandler;
            clientHandler.start();

            clientNumUidMap.put(clientNumber, client.getUid());
            clients.put(client.getUid(), client);
        } else {
            //Client was already registered and this is a repeated message
            client = clients.get(clientNumUidMap.get(helloMessage.getRandomKey()));
        }

        //Send back acknowledgement
        AcknowledgeMessage acknowledgeMessage = new AcknowledgeMessage(0L, client.getUid());
        byte[] sendData = MessageUtil.serializeMessage(acknowledgeMessage);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
        sendPacket.setSocketAddress(client.getAddress());
        socket.send(sendPacket);
    }

    /**
     * Switching off server.
     */
    public void close() {
        socket.close();
    }

    /**
     * Main method that takes one argument the port of the server and starts it.
     *
     * @param args one argument
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Expecting 1 argument!");
            return;
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server;
        try {
            server = new ChatServer(port);
            server.start();
            server.close();
        } catch (SocketException e) {
            System.out.println("Server socket creating error: " + e.getMessage());
        }
    }

    /**
     * Client class for connection management.
     */
    private static final class Client {
        private final long randomNumber;
        private final long uid;
        private final String name;
        private final SocketAddress address;
        //Clients acknowledgement messages queue
        private final BlockingQueue<AcknowledgeMessage> acknowledgeMessages = new LinkedBlockingQueue<>();
        //Client outbound message queue
        private final BlockingQueue<OutMessage> outMessages = new LinkedBlockingQueue<>();

        private long clientMessageNumber;
        private long serverMessageNumber;
        //Thread that handles sending new messages to client and their confirmations.
        private Thread clientHandler;

        private Client(long randomNumber, long uid, String name, SocketAddress address,
                       long clientMessageNumber, long serverMessageNumber) {
            this.randomNumber = randomNumber;
            this.uid = uid;
            this.name = name;
            this.address = address;
            this.clientMessageNumber = clientMessageNumber;
            this.serverMessageNumber = serverMessageNumber;
        }

        public long getRandomNumber() {
            return randomNumber;
        }

        public long getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public SocketAddress getAddress() {
            return address;
        }

        public long getClientMessageNumber() {
            return clientMessageNumber;
        }

        public long getClientServerNumber() {
            return serverMessageNumber;
        }

    }

    /**
     * Class that implements runnable and is there to handle sending new messages to client and their acknowledgements.
     */
    private record ClientHandler(Client client,
                                 Map<Long, Client> clientMap,
                                 DatagramSocket socket) implements Runnable {

        @Override
        public void run() {

            while (true) {
                OutMessage outMessage;
                try {
                    outMessage = client.outMessages.take();
                } catch (InterruptedException ignore) {
                    continue;
                }

                InMessage broadcastMessage = new InMessage(client.getClientServerNumber(),
                        clientMap.get(outMessage.getUid()).name,
                        outMessage.getMessage());

                boolean sent;
                try {
                    sent = sendMessageAndRetry(client.getAddress(), broadcastMessage, client.acknowledgeMessages, socket);
                } catch (IOException e) {
                    System.out.println("Sending error: " + e.getMessage());
                    return;
                }
                if (sent) client.serverMessageNumber++;
            }

        }
    }

    /**
     * Method that sends message to address and retries 10 times if acknowledgement not received for 5 seconds.
     * @param address address to send message to
     * @param message message to send
     * @param acknowledgeMessages blocking queue from where to check new messages
     * @param socket socket from which to send message
     * @return true if successfully sent else false
     * @throws IOException if error while sending message
     */
    private static boolean sendMessageAndRetry(SocketAddress address,
                                               Message message,
                                               BlockingQueue<AcknowledgeMessage> acknowledgeMessages,
                                               DatagramSocket socket) throws IOException {
        byte[] data;
        data = MessageUtil.serializeMessage(message);
        DatagramPacket outboundPacket = new DatagramPacket(data, data.length);
        outboundPacket.setSocketAddress(address);

        int retries = 10;
        while (retries > 0) {
            retries--;

            try {
                socket.send(outboundPacket);
            } catch (IOException e) {
                System.out.println("Sending error: " + e.getMessage());
                continue;
            }

            AcknowledgeMessage acknowledgeMessage;

            try {
                acknowledgeMessage = acknowledgeMessages.poll(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.out.println("Acknowledge message not received. Trying again.");
                continue;
            }

            if (acknowledgeMessage != null && message.getMessageId() == acknowledgeMessage.getMessageId()) return true;

            System.out.println("Acknowledge message number invalid. Trying again.");
        }
        return false;
    }

}
