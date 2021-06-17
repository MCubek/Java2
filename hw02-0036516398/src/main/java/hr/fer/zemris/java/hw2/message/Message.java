package hr.fer.zemris.java.hw2.message;

/**
 * Abstract class for server-client Chat app messages.
 * Class that extend this abstract class represent all types of communication between
 * client and server.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public abstract class Message {
    //Type enumeration
    private final MessageType messageType;
    //Sequential id of message
    private final long messageId;

    public Message(MessageType messageType, long messageId) {
        this.messageType = messageType;
        this.messageId = messageId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public long getMessageId() {
        return messageId;
    }

}
