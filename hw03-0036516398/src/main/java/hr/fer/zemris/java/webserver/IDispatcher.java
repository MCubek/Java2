package hr.fer.zemris.java.webserver;

/**
 * Interface for dispatching http requests.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 30/03/2021
 */
public interface IDispatcher {

    /**
     * Method that dispatches request to given url.
     *
     * @param urlPath url of requested file
     * @throws Exception if error occurs while dispatching request
     */
    void dispatchRequest(String urlPath) throws Exception;
}
