package hr.fer.zemris.java.hw2.message;

/**
 * Enumeration of message types class.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public enum MessageType {
    HELLO(1),
    ACK(2),
    BYE(3),
    OUTMSG(4),
    INMSG(5);

    //Code below for helping with parsing UDP packets
    private final int number;

    MessageType(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static MessageType getTypeFromNumber(int number) {
        return switch (number) {
            case 1 -> HELLO;
            case 2 -> ACK;
            case 3 -> BYE;
            case 4 -> OUTMSG;
            case 5 -> INMSG;
            default -> throw new IllegalStateException("Unexpected message number: " + number);
        };
    }


}

