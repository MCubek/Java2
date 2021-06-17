package hr.fer.zemris.java.hw2.message;

/**
 * Message that is created when client wants to send something to the server and all other connected clients.
 * Only client can send this type of message.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public class OutMessage extends Message {
    //Client uid
    private final long uid;
    private final String message;

    public OutMessage(long messageId, long uid, String message) {
        super(MessageType.OUTMSG, messageId);
        this.uid = uid;
        this.message = message;
    }

    public long getUid() {
        return uid;
    }

    public String getMessage() {
        return message;
    }

}
