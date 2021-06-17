package hr.fer.zemris.java.webserver;

/**
 * Web server worker for dynamic HTML content.
 * Does stuff with context.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 30/03/2021
 */
@SuppressWarnings("unused")
public interface IWebWorker {
    /**
     * Method that processes request with context.
     *
     * @param context context on which request is processed.
     * @throws Exception when error occurs while processing.
     */
    void processRequest(RequestContext context) throws Exception;
}
