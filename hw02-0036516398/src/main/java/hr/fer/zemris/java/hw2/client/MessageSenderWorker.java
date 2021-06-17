package hr.fer.zemris.java.hw2.client;

import hr.fer.zemris.java.hw2.message.AcknowledgeMessage;
import hr.fer.zemris.java.hw2.message.Message;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SwingWorker class that sends messages to server and waits for it's acknowledgement.
 * When sending message and waiting for response worker disables the text input
 * and enables and clears it when confirmation from server is received.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 19/03/2021
 */
public class MessageSenderWorker extends SwingWorker<Boolean, Void> {
    private final Message message;
    private final InetAddress serverAddress;
    private final int serverPort;
    private final DatagramSocket clientSocket;

    private final JTextField field;
    private final AtomicLong clientCounter;
    private final BlockingQueue<AcknowledgeMessage> acknowledgeMessages;

    public MessageSenderWorker(Message message,
                               InetAddress serverAddress,
                               int serverPort,
                               DatagramSocket clientSocket,
                               JTextField field,
                               AtomicLong clientCounter,
                               BlockingQueue<AcknowledgeMessage> acknowledgeMessages) {
        this.message = message;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.clientSocket = clientSocket;
        this.field = field;
        this.clientCounter = clientCounter;
        this.acknowledgeMessages = acknowledgeMessages;
    }

    @Override
    protected Boolean doInBackground() {
        field.setEnabled(false);

        // Try to send message
        int retries = 10;
        while (retries != 0) {
            retries--;

            try {
                ChatClient.sendMessage(clientSocket,serverAddress,serverPort,message);
            } catch (IOException e) {
                System.out.println("Message sending error!");
                continue;
            }

            //Wait for acknowledgement
            AcknowledgeMessage acknowledgeMessage;
            try {
                acknowledgeMessage = acknowledgeMessages.poll(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) {
                continue;
            }

            if (acknowledgeMessage == null) {
                System.out.println("No acknowledgement received for message. Sending again.");
                continue;
            }

            if (message.getMessageId() != acknowledgeMessage.getMessageId()) {
                System.out.println("Wrong acknowledgement received. Sending again.");
            }

            return true;
        }
        return false;
    }

    @Override
    protected void done() {
        try {
            //If message is sent successfully
            if (get()) {
                clientCounter.getAndIncrement();
                field.setText("");
            }

            //Also enables when not able to send message
            //But doesnt empty the text box
            field.setEnabled(true);
            field.requestFocusInWindow();
        } catch (InterruptedException | ExecutionException ignore) {
            //When error happens behave same as when unable to send message
            field.setEnabled(true);
            field.requestFocusInWindow();
        }
    }

}
