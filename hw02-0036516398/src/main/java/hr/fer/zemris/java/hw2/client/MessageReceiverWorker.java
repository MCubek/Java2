package hr.fer.zemris.java.hw2.client;

import hr.fer.zemris.java.hw2.message.AcknowledgeMessage;
import hr.fer.zemris.java.hw2.message.InMessage;
import hr.fer.zemris.java.hw2.message.Message;
import hr.fer.zemris.java.hw2.message.MessageUtil;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * Swing worker class that actively listens for incoming messages from server and handles them.
 * When encountering acknowledgement message it's added to blocking queue and when in message
 * it appends it to the gui's text area.
 * @see SwingWorker
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 19/03/2021
 */
public class MessageReceiverWorker extends SwingWorker<Void, InMessage> {
    private final InetAddress serverAddress;
    private final int serverPort;
    private final DatagramSocket clientSocket;
    private final long uid;

    private final JTextArea textArea;

    private long serverCounter;
    private final BlockingQueue<AcknowledgeMessage> acknowledged;


    public MessageReceiverWorker(InetAddress serverAddress,
                                 int serverPort,
                                 DatagramSocket clientSocket,
                                 long uid,
                                 JTextArea textArea,
                                 BlockingQueue<AcknowledgeMessage> acknowledged) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.clientSocket = clientSocket;
        this.uid = uid;
        this.textArea = textArea;
        this.acknowledged = acknowledged;

        this.serverCounter = 0L;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while (true) {
            if (isCancelled()) return null;

            byte[] responseData = new byte[300];
            DatagramPacket response = new DatagramPacket(responseData, responseData.length);

            //Try to receive a response
            try {
                clientSocket.receive(response);
            } catch (SocketTimeoutException timeoutException) {
                continue;
            } catch (IOException e) {
                if (isCancelled()) return null;

                System.out.println("Receive error: " + e.getMessage());
                continue;
            }

            //Parse response data
            Message serverMessage;
            try {
                serverMessage = MessageUtil.deserializeMessage(response.getData());
            } catch (IOException e) {
                System.out.println("Message parsing exception!");
                continue;
            }

            if (serverMessage instanceof AcknowledgeMessage acknowledge) {
                acknowledged.put(acknowledge);

            } else if (serverMessage instanceof InMessage inMessage) {

                //Update counter and publish new message
                if (inMessage.getMessageId() > serverCounter) {
                    serverCounter = inMessage.getMessageId();
                    publish(inMessage);
                }

                //Send AcknowledgeMessage for any message
                byte[] sendData = MessageUtil.serializeMessage(new AcknowledgeMessage(inMessage.getMessageId(), uid));
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);

                sendPacket.setAddress(serverAddress);
                sendPacket.setPort(serverPort);

                try {
                    clientSocket.send(sendPacket);
                } catch (IOException e) {
                    System.out.println("Acknowledge sending error. Continuing.");
                }

            } else
                System.out.println("Wrong type of message received.");
        }
    }

    @Override
    protected void process(List<InMessage> chunks) {

        //Add message to GUI
        textArea.append(chunks.stream()
                .map(msg -> String.format("[/%s:%d] Message from user: %s\n%s\n\n",
                        serverAddress.getHostAddress(),
                        serverPort,
                        msg.getUsername(),
                        msg.getMessage()))
                .collect(Collectors.joining("\n")));
    }
}
