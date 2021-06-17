package hr.fer.oprpp2.hw6.blog.web.servlets.blog;

import hr.fer.oprpp2.hw6.blog.dao.DAOProvider;
import hr.fer.oprpp2.hw6.blog.model.BlogUser;
import hr.fer.oprpp2.hw6.blog.model.form.BlogUserForm;
import hr.fer.oprpp2.hw6.blog.utils.Common;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for user registration.
 * Users that are logged in will not be allowed to access.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
@WebServlet(name = "Register", value = "/servleti/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Do not allow logged in users to access
        if ((Long) req.getAttribute("currentID") >= 0) {
            Common.sendErrorMessage(req, resp, "Please log out before registration!");
            return;
        }

        req.setAttribute("errorMessage", "");
        req.setAttribute("form", new BlogUserForm());
        req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Do not allow logged in users to access
        if ((Long) req.getAttribute("currentID") >= 0) {
            Common.sendErrorMessage(req, resp, "Please log out before registration!");
            return;
        }

        //Populate form from request
        BlogUserForm userForm = new BlogUserForm();
        userForm.populateFromHttpRequest(req);

        userForm.validate();
        if (userForm.hasErrors()) {
            sendFormErrorMessage(req, resp, userForm.getErrors(), userForm);
            return;
        }

        //Check if user exists with nickname from form
        BlogUser user = DAOProvider.getDAO().getBlogUserByNick(userForm.getNick());
        if (user != null) {
            sendFormErrorMessage(req, resp, "User with nickname already exists!", userForm);
            return;
        }
        //Create new user and populate it from form
        user = new BlogUser();
        userForm.populateIntoBlogUser(user);

        DAOProvider.getDAO().persistBlogUser(user);

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }

    /**
     * Helper method for sending error into current jsp page
     *
     * @param req      request
     * @param resp     response
     * @param message  message to send
     * @param userForm user form to send back data from
     * @throws ServletException when error occurs
     * @throws IOException      when error occurs
     */
    private void sendFormErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message, BlogUserForm userForm) throws ServletException, IOException {
        req.setAttribute("form", userForm);
        req.setAttribute("errorMessage", message);
        req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
    }
}
