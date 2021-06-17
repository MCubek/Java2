package hr.fer.oprpp2.hw6.blog.dao;

import java.io.Serial;

/**
 * Exception that signals error with DAO command.
 */
public class DAOException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {
        super(message);
    }
}