package hr.fer.oprpp2.hw6.blog.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Util class with common static methods.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
public class Common {
    /**
     * Static method for sending custom error message to client.
     *
     * @param req     request
     * @param resp    response
     * @param message message to send
     * @throws ServletException servlet error
     * @throws IOException      io error
     */
    public static void sendErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("MESSAGE", message);
        req.getRequestDispatcher("/WEB-INF/pages/ErrorMessage.jsp").forward(req, resp);
    }

}
