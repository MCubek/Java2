package hr.fer.zemris.java.hw2.message;

/**
 * Message that represents the end of client communication and shutting down.
 * Message will always be sent by client and acknowledged from server.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public class ByeMessage extends Message {
    private final long uid;

    public ByeMessage(long messageId, long uid) {
        super(MessageType.BYE, messageId);
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

}
