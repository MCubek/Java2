package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Iznimka koja ukazuje na gršku kod tokenizacije
 */
public class SmartScriptLexerException extends RuntimeException {
    public SmartScriptLexerException() {
        super();
    }

    public SmartScriptLexerException(String message) {
        super(message);
    }

    public SmartScriptLexerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartScriptLexerException(Throwable cause) {
        super(cause);
    }
}
