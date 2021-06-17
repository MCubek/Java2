package hr.fer.oprpp2.hw5.servlets.polls;

import hr.fer.oprpp2.hw5.dao.DAOProvider;
import hr.fer.oprpp2.hw5.model.Poll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for showing list of all polls
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 14/05/2021
 */
@WebServlet(name = "PollsIndex", urlPatterns = "/servleti/index.html")
public class PollsIndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Poll> polls = DAOProvider.getDao().getPollList();

        req.setAttribute("polls", polls);

        req.getRequestDispatcher("/WEB-INF/pages/pollsIndex.jsp").forward(req, resp);
    }
}
