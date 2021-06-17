package hr.fer.oprpp2.hw6.blog.web.servlets.blog;

import hr.fer.oprpp2.hw6.blog.dao.DAOProvider;
import hr.fer.oprpp2.hw6.blog.model.BlogUser;
import hr.fer.oprpp2.hw6.blog.utils.Crypto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Main servlet for login and for the homepage.
 * GET request serves the hompage and POST request
 * is used for login.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */

@WebServlet(name = "LoginMain", value = "/servleti/main")
public class LoginMainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BlogUser> list = DAOProvider.getDAO().getBlogUsers();

        req.setAttribute("authors", list);
        req.setAttribute("errorMessage", "");

        req.getRequestDispatcher("/WEB-INF/pages/MainLogin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nickname = req.getParameter("nick");
        String password = req.getParameter("password");

        //Check if parameters are filled
        if (nickname == null || password == null || nickname.isBlank() || password.isBlank()) {
            sendFormErrorMessage(req, resp, "Nickname or password empty!");
            return;
        }

        //Check if user exists and if passwords match
        BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nickname);
        if (user == null || ! isPasswordValid(password, user.getPasswordHash())) {
            sendFormErrorMessage(req, resp, "Username or password invalid!");
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("current.user.id", user.getId());
        session.setAttribute("current.user.fn", user.getFirstName());
        session.setAttribute("current.user.ln", user.getLastName());
        session.setAttribute("current.user.nick", user.getNick());

        req.setAttribute("errorMessage", "");
        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }

    /**
     * Helper method for comparing password with stored hash
     *
     * @param givenPassword given password
     * @param storedHash    stored hash
     * @return <code>true</code> if valid else <code>false</code>
     */
    private static boolean isPasswordValid(String givenPassword, byte[] storedHash) {
        byte[] givenPasswordHash = Crypto.calculateDigest(givenPassword);

        return Arrays.equals(givenPasswordHash, storedHash);
    }

    /**
     * Helper method for sending error into same page.
     *
     * @param req request
     * @param resp response
     * @param message message to send
     * @throws ServletException when error occurs
     * @throws IOException      when error occurs
     */
    private void sendFormErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        List<BlogUser> list = DAOProvider.getDAO().getBlogUsers();
        req.setAttribute("authors", list);
        req.getRequestDispatcher("/WEB-INF/pages/MainLogin.jsp").forward(req, resp);
    }

}
