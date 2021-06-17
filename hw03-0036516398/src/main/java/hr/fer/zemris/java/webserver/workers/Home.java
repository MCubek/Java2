package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker draws homepage with custom backgroud color found in client's
 * session context.
 * If color is not found in context grey is set.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 31/03/2021
 */
public class Home implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String color = context.getPersistentParameter("bgcolor");
        if (color == null) color = "7F7F7F";
        context.setTemporaryParameter("background", color);

        context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
    }
}
