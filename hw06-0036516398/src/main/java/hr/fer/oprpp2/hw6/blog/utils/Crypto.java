package hr.fer.oprpp2.hw6.blog.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cryptography class containing static cryptography methods.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
public class Crypto {

    /**
     * Method for calculating SHA-1 digest for given string.
     * Method returns digest as array of bytes.
     *
     * @param input input string
     * @return byte array containing digest
     */
    public static byte[] calculateDigest(String input) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            //Not able to be envoked by user
            throw new RuntimeException("Algorithm doesn't exist");
        }

        return messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
    }
}
