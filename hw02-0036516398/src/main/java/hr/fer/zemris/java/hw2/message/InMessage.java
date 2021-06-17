package hr.fer.zemris.java.hw2.message;

/**
 * Message that server sends to clients when a new message has arrived and
 * it has to be shown on the client's gui.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public class InMessage extends Message {
    private final String username;
    private final String message;

    public InMessage(long messageId, String username, String message) {
        super(MessageType.INMSG, messageId);
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
