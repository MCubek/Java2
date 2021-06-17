package hr.fer.oprpp2.hw4.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet used for changing background color of all pages
 * by accepting argument and saving it in session under pickedBgCol.
 * Servlet accepts all valid hex color codes sent as path after url.
 * When color is stored servlet redirects client to colors page.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 08/04/2021
 */
@WebServlet(name = "SetColor", urlPatterns = "/setcolor/*")
public class ColorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String color = req.getPathInfo();
        if (color != null && color.startsWith("/")) {
            color = color.substring(1);
        }

        if (color == null || ! color.matches("[0-9a-fA-F]{6}")) {
            sendErrorMessage(req, resp, "Color is invalid.");
            return;
        }

        session.setAttribute("pickedBgCol", "#" + color);

        resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/colors.jsp"));
    }

    @SuppressWarnings("SameParameterValue")
    private void sendErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("MESSAGE", message);
        req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
    }
}
