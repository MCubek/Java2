package hr.fer.oprpp2.hw5.dao;

import java.io.Serial;

public class DAOException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public DAOException() {
	}

	public DAOException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}
}