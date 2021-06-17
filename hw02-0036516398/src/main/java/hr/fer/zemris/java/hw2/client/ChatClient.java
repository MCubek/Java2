package hr.fer.zemris.java.hw2.client;

import hr.fer.zemris.java.hw2.message.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Chat client for cool chit-chat with your buddies in this simple java Swing app.
 * Chat connects to server via UDP and has a awesome sophisticated GUI interface.
 * Don't forget to keep track of time when enjoying yourself with this amazing application.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public class ChatClient extends JFrame {

    private final InetAddress serverAddress;
    private final int serverPort;
    private final DatagramSocket clientSocket;
    private final long uid;

    private final AtomicLong clientCount = new AtomicLong(1L);
    private final BlockingQueue<AcknowledgeMessage> acknowledged;

    private JTextField inputField;
    private JTextArea textArea;

    private final MessageReceiverWorker receiver;

    public ChatClient(InetAddress serverAddress, int serverPort, String name, DatagramSocket socket, long uid) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.clientSocket = socket;
        this.uid = uid;

        this.acknowledged = new LinkedBlockingDeque<>();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Cool Chat: " + name);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        initGUI();

        //Message receiver thread
        receiver = new MessageReceiverWorker(serverAddress,
                serverPort,
                clientSocket,
                uid,
                textArea,
                acknowledged
        );
        receiver.execute();
    }

    private void initGUI() {
        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        inputField = new JTextField();
        textArea = new JTextArea();
        textArea.setEditable(false);

        inputField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendAsyncMessageToServer(new OutMessage(clientCount.get(), uid, inputField.getText()));
            }
        });

        container.add(inputField, BorderLayout.PAGE_START);
        container.add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    private void exit() {
        receiver.cancel(true);

        try {
            sendMessage(clientSocket, serverAddress, serverPort, new ByeMessage(clientCount.get(), uid));
        } catch (IOException ignore) {
        }

        dispose();
    }

    private void sendAsyncMessageToServer(Message message) {
        new MessageSenderWorker(message,
                serverAddress,
                serverPort,
                clientSocket,
                inputField,
                clientCount,
                acknowledged
        ).execute();
    }

    /**
     * Main method that runs the chat client.
     * Method requires following parameters:
     * 1) Server ip address
     * 2) Server port
     * 3) Client name or username
     *
     * @param args 1) Server ip address
     *             2) Server port
     *             3) Client name or username
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Expecting 3 arguments!");
            return;
        }

        //Address parsing
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("Address invalid!");
            return;
        }
        //Port parsing
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Port invalid value!");
            return;
        }
        //Client name parsing
        String name = args[2];

        //Create local socket
        DatagramSocket datagramSocket;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Socket creating error: " + e.getMessage());
            return;
        }

        long uid = connectAndRetrieveUID(name, inetAddress, port, datagramSocket);

        if (uid == 0) {
            System.out.println("Couldn't connect to server.");
            return;
        }

        ChatClient chatClient = new ChatClient(inetAddress, port, name, datagramSocket, uid);

        SwingUtilities.invokeLater(() -> chatClient.setVisible(true));
    }

    /**
     * Method that tries sends the first, hello message to the server with an address and port specified and waits for its
     * response in form of a acknowledge message that carries an UID for the chat client.
     * Method waits for 5 seconds after sending message for a response and if it doesn't get one it times out.
     * After 10 tries or timeouts method returns a uid of zero when unable to connect to server.
     *
     * @param name          client name/username
     * @param serverAddress server ip address
     * @param serverPort    server port
     * @param clientSocket  clients datagram socket
     * @return uid which server returns or 0 when unable to contact server
     */
    private static long connectAndRetrieveUID(String name,
                                              InetAddress serverAddress,
                                              int serverPort,
                                              DatagramSocket clientSocket) {

        HelloMessage helloMessage = new HelloMessage(name);

        var response = sendMessageAndReturnResponseSync(helloMessage, serverAddress, serverPort, clientSocket);

        if (response == null) return 0L;

        return response.getUid();
    }


    /**
     * Method that tries send a message to the server with an address and port specified
     * and waits for its response in form of a acknowledge message.
     * Method waits for 5 seconds after sending message for a response and if it doesn't get one it times out.
     * After 10 tries or timeouts method returns null when unable to connect to server or
     * the AcknowledgeMessage when it has heard back properly.
     *
     * @param messageToSend Message to send to the server
     * @param serverAddress Server's address
     * @param serverPort    Server's port
     * @param clientSocket  Client's socket
     * @return AcknowledgeMessage when it is received or null otherwise
     */
    public static AcknowledgeMessage sendMessageAndReturnResponseSync(Message messageToSend,
                                                                      InetAddress serverAddress,
                                                                      int serverPort,
                                                                      DatagramSocket clientSocket) {
        int retries = 10;
        while (retries != 0) {
            retries--;

            //Send message
            try {
                sendMessage(clientSocket, serverAddress, serverPort, messageToSend);
            } catch (IOException e) {
                System.out.println("Sending error!");
                continue;
            }

            //Set timeout
            try {
                clientSocket.setSoTimeout(5000);
            } catch (SocketException e) {
                System.out.println("Socket error: " + e.getMessage());
                continue;
            }

            byte[] responseData = new byte[300];
            DatagramPacket response = new DatagramPacket(responseData, responseData.length);

            //Try to receive a response
            try {
                clientSocket.receive(response);
            } catch (SocketTimeoutException timeoutException) {
                System.out.println("Timed out!");
                continue;
            } catch (IOException e) {
                System.out.println("Receive error: " + e.getMessage());
                continue;
            }

            //Parse response data
            AcknowledgeMessage acknowledgeMessage;
            try {
                if (MessageUtil.deserializeMessage(response.getData()) instanceof AcknowledgeMessage ack) {
                    acknowledgeMessage = ack;
                } else {
                    System.out.println("Wrong response message type!");
                    continue;
                }
            } catch (IOException e) {
                continue;
            }

            if (acknowledgeMessage.getMessageId() != messageToSend.getMessageId()) {
                System.out.println("Wrong message acknowledged!");
                continue;
            }

            return acknowledgeMessage;
        }
        return null;
    }

    /**
     * Method that sends a message to the address from parameters.
     * Method doesn't check for acknowledgement.
     *
     * @param clientSocket socket from which to send message
     * @param destinationAddress destination ip address
     * @param destinationPort destination port
     * @param message message to send
     * @throws IOException when error while sending or error while serializing message
     */
    public static void sendMessage(DatagramSocket clientSocket, InetAddress destinationAddress, int destinationPort, Message message) throws IOException {
        byte[] sendData;
        sendData = MessageUtil.serializeMessage(message);

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
        sendPacket.setAddress(destinationAddress);
        sendPacket.setPort(destinationPort);

        clientSocket.send(sendPacket);
    }
}
