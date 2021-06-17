package hr.fer.oprpp2.hw5.servlets.polls;

import hr.fer.oprpp2.hw5.dao.DAOProvider;
import hr.fer.oprpp2.hw5.model.Poll;
import hr.fer.oprpp2.hw5.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static hr.fer.oprpp2.hw5.utils.Common.sendErrorMessage;

/**
 * Servlet for voting on given poll in request parameter.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 14/05/2021
 */
@WebServlet(name = "PollVoting", urlPatterns = "/servleti/glasanje")
public class PollVotingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long pollID;
        try {
            pollID = Long.parseLong(req.getParameter("pollID"));
        } catch (NumberFormatException exception) {
            sendErrorMessage(req, resp, "Invalid poll selected!");
            return;
        }

        Poll poll = DAOProvider.getDao().getPollById(pollID);
        List<PollOption> candidates = DAOProvider.getDao().getPollOptionsForPoll(pollID);

        req.setAttribute("poll", poll);
        req.setAttribute("candidates", candidates);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }
}
