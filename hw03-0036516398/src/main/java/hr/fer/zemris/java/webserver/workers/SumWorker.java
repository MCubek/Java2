package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker draws cool picture of a animal and calculates two numbers's <code>a & b</code>
 * sum. Different picture is shown depenging on whether sum is even or nor.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 30/03/2021
 */
public class SumWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        int a, b;
        try {
            a = Integer.parseInt(context.getParameter("a"));
        } catch (NumberFormatException | NullPointerException exception) {
            a = 1;
        }
        try {
            b = Integer.parseInt(context.getParameter("b"));
        } catch (NumberFormatException | NullPointerException exception) {
            b = 1;
        }

        int sum = a + b;
        String image = sum % 2 == 0 ? "kc.jpg" : "shrek.jpg";
        context.setTemporaryParameter("varA", String.valueOf(a));
        context.setTemporaryParameter("varB", String.valueOf(b));
        context.setTemporaryParameter("zbroj", String.valueOf(sum));
        context.setTemporaryParameter("imgName", image);

        context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
    }
}
