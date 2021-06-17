package hr.fer.zemris.java.hw2.message;

/**
 * Message that represents acknowledgement of a previous message in chat.
 * Both client and server can send this type of message.
 * Message contains uid of user to which it is sent or who is the one sending
 * and it contains messageId of message it acknowledges.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public class AcknowledgeMessage extends Message {
    private final long uid;

    public AcknowledgeMessage(long messageId, long uid) {
        super(MessageType.ACK, messageId);
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

}
