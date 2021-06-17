package hr.fer.oprpp1.custom.collections;

/**
 * EmptyStackException je podklasa <code>RuntimeException</code> i spada u neprovjeravane iznimke.
 * Koristi se kada je potrebno pokazati da je stog prazan a traži se element s njega.
 *
 * @author matej
 */
public class EmptyStackException extends RuntimeException {
    public EmptyStackException() {
    }

    public EmptyStackException(String message) {
        super(message);
    }

    public EmptyStackException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyStackException(Throwable cause) {
        super(cause);
    }
}
