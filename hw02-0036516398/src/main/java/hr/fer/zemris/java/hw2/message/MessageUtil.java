package hr.fer.zemris.java.hw2.message;

import java.io.*;

/**
 * Utility class for handling messages.
 * Class contains static methods for serialization and deserialization of messages so
 * they cant be sent as a datagram packet.
 *
 * @author MatejCubek
 * @project hw02-0036516398
 * @created 18/03/2021
 */
public class MessageUtil {
    private MessageUtil() {
    }

    /**
     * Serialization of message that creates array of bytes ready to send via UDP datagram packet.
     *
     * @param message message to serialize
     * @return <code>byte[]</code> containing the message
     * @throws IOException when error occurs while serializing
     */
    public static byte[] serializeMessage(Message message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeByte(message.getMessageType().getNumber());
        dos.writeLong(message.getMessageId());

        switch (message.getMessageType()) {
            case HELLO -> {
                HelloMessage helloMessage = (HelloMessage) message;
                dos.writeUTF(helloMessage.getUsername());
                dos.writeLong(helloMessage.getRandomKey());
            }
            case ACK -> {
                AcknowledgeMessage acknowledgeMessage = (AcknowledgeMessage) message;
                dos.writeLong(acknowledgeMessage.getUid());
            }
            case BYE -> {
                ByeMessage byeMessage = (ByeMessage) message;
                dos.writeLong(byeMessage.getUid());
            }
            case OUTMSG -> {
                OutMessage outMessage = (OutMessage) message;
                dos.writeLong(outMessage.getUid());
                dos.writeUTF(outMessage.getMessage());
            }
            case INMSG -> {
                InMessage inMessage = (InMessage) message;
                dos.writeUTF(inMessage.getUsername());
                dos.writeUTF(inMessage.getMessage());
            }
        }

        dos.close();
        return bos.toByteArray();
    }

    /**
     * Deserialization of message that creates a <code>Message</code> object from received bytes[]
     *
     * @param buf received bytes[]
     * @return <code>Message</code> of right type deserialized from <code>buf</code>
     * @throws IOException when error occurs while deserializing
     * @see Message
     */
    public static Message deserializeMessage(byte[] buf) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(buf);
        DataInputStream dos = new DataInputStream(bis);

        MessageType messageType = MessageType.getTypeFromNumber(dos.readByte());
        long id = dos.readLong();

        Message message;

        message = switch (messageType) {
            case HELLO -> new HelloMessage(id, dos.readUTF(), dos.readLong());
            case ACK -> new AcknowledgeMessage(id, dos.readLong());
            case BYE -> new ByeMessage(id, dos.readLong());
            case OUTMSG -> new OutMessage(id, dos.readLong(), dos.readUTF());
            case INMSG -> new InMessage(id, dos.readUTF(), dos.readUTF());
        };

        dos.close();

        return message;
    }
}
