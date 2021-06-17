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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static hr.fer.oprpp2.hw5.utils.Common.sendErrorMessage;

/**
 * Servlet for vote results.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 14/05/2021
 */
@SuppressWarnings("SpellCheckingInspection")
@WebServlet(name = "PollResult", urlPatterns = "/servleti/glasanje-rezultati")
public class PollResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long pollID;
        try {
            pollID = Long.parseLong(req.getParameter("PollID"));
        } catch (NumberFormatException exception) {
            sendErrorMessage(req, resp, "Invalid poll selected!");
            return;
        }

        Poll poll = DAOProvider.getDao().getPollById(pollID);

        if (poll == null) {
            sendErrorMessage(req, resp, "Poll doesn't exist!");
            return;
        }

        List<PollOption> candidates = DAOProvider.getDao().getPollOptionsForPoll(pollID).stream()
                .sorted(Comparator.comparing(PollOption::getVotesCount).reversed())
                .collect(Collectors.toList());

        req.setAttribute("pollId", pollID);
        req.setAttribute("candidates", candidates);
        req.setAttribute("winners", getWinners(candidates));

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }

    /**
     * Gets winners from list of candidates
     *
     * @param candidates list of candidates
     * @return list of winners
     */
    private static List<PollOption> getWinners(List<PollOption> candidates) {
        long maxVotes = candidates.stream()
                .mapToLong(PollOption::getVotesCount)
                .max().orElse(0);

        return candidates.stream()
                .filter(c -> c.getVotesCount().equals(maxVotes))
                .collect(Collectors.toList());
    }
}
