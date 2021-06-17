package hr.fer.oprpp2.hw6.blog.web.servlets.blog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for logging out and clearing session.
 * Servlet redirects to homepage.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
@WebServlet(name = "Logout", value = "/servleti/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }
}
