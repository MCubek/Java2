package hr.fer.oprpp2.hw4.servlets.voting;

import hr.fer.oprpp2.hw4.utils.voting.VoteFileManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Servlet for accepting votes for bands.
 * Servlet doesn't check if band exists when saving vote for performance
 * reasons.
 * When vote is stored servlet redirects client to results page.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "Vote", urlPatterns = "/glasanje-glasaj")
public class VotingVoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long voteId;
        try {
            voteId = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException exception) {
            sendErrorMessage(req, resp, "Invalid vote cast!");
            return;
        }

        String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        Path votesPath = Path.of(fileName);

        VoteFileManager.addVote(votesPath, voteId);

        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }

    @SuppressWarnings("SameParameterValue")
    private void sendErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("MESSAGE", message);
        req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
    }
}
