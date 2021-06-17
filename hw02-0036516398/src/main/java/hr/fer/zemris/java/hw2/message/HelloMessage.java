package hr.fer.zemris.java.hw2.message;

import java.util.Random;

/**
 * Message that establishes connection from client to server.
 * Message is always sent by client and acknowledged by server.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public class HelloMessage extends Message {
    //Clients username
    private final String username;
    //Random key that client who sends message generates so server can distinguish
    private final long randomKey;

    /**
     * Constructor for reconstructing messages that have come via packets
     *
     * @param messageId message id
     * @param username  client username/name
     * @param randomKey random long key for client
     */
    public HelloMessage(long messageId, String username, long randomKey) {
        super(MessageType.HELLO, messageId);
        this.username = username;
        this.randomKey = randomKey;
    }

    /**
     * Constructor for a new Hello message
     *
     * @param username client username/name
     */
    public HelloMessage(String username) {
        this(0, username, new Random().nextLong());
    }

    public String getUsername() {
        return username;
    }

    public long getRandomKey() {
        return randomKey;
    }
}
