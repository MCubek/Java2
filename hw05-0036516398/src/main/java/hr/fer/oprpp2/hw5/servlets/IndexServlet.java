package hr.fer.oprpp2.hw5.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Index servlet used for redirecting.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 14/05/2021
 */
@WebServlet(name = "IndexServlet", urlPatterns = "/index.html")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/servleti/index.html");
    }
}
