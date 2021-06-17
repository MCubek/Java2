package hr.fer.oprpp2.hw6.blog.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Index servlet for redirecting to main servlet from index homepage.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
@WebServlet(name = "IndexServlet", urlPatterns = "/index.jsp")
public class Index extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }
}
