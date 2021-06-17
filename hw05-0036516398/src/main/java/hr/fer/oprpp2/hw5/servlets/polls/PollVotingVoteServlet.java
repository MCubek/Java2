package hr.fer.oprpp2.hw5.servlets.polls;

import hr.fer.oprpp2.hw5.dao.DAOProvider;
import hr.fer.oprpp2.hw5.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static hr.fer.oprpp2.hw5.utils.Common.sendErrorMessage;

/**
 * Servlet for casting votes to candidate.
 * After casting vote server redirects to poll's result page.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 14/05/2021
 */
@SuppressWarnings("SpellCheckingInspection")
@WebServlet(name = "PollVote", urlPatterns = "/servleti/glasanje-glasaj")
public class PollVotingVoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long candidateId;
        try {
            candidateId = Long.parseLong(req.getParameter("ID"));
        } catch (NumberFormatException exception) {
            sendErrorMessage(req, resp, "Invalid candidate selected!");
            return;
        }
        PollOption candidate = DAOProvider.getDao().getPollOptionById(candidateId);

        if (candidate == null) {
            sendErrorMessage(req, resp, "No such candidate.");
            return;
        }

        DAOProvider.getDao().castVoteToPollOption(candidateId);

        resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?PollID=" + candidate.getPollId());
    }
}
