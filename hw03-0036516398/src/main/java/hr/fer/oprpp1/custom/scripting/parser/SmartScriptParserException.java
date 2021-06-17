package hr.fer.oprpp1.custom.scripting.parser;

/**
 * Iznimka parsiranja
 *
 * @author matej
 */
public class SmartScriptParserException extends RuntimeException {
    public SmartScriptParserException() {
    }

    public SmartScriptParserException(String message) {
        super(message);
    }

    public SmartScriptParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartScriptParserException(Throwable cause) {
        super(cause);
    }
}
