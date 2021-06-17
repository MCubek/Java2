package hr.fer.oprpp2.hw4.servlets.voting;

import hr.fer.oprpp2.hw4.utils.voting.Band;
import hr.fer.oprpp2.hw4.utils.voting.VoteFileManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servlet for displaying voting results page.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "VotingResults", urlPatterns = "/glasanje-rezultati")
public class VotingResultsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bandsFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        String votesFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        Path bandsPath = Path.of(bandsFile);
        Path votesPath = Path.of(votesFile);

        var bandVotes = VoteFileManager.getSortedVotes(bandsPath, votesPath);
        var winners = getWinners(bandVotes);

        req.setAttribute("bands_votes", bandVotes);
        req.setAttribute("winners", winners);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }

    /**
     * Helper method for returning list of bands, winners.
     *
     * @param map map of bands and their votes.
     * @return list containg bands that are winners.
     */
    private static List<Band> getWinners(Map<Band, Integer> map) {
        int maxVotes = map.values().stream()
                .mapToInt(v -> v)
                .max().orElse(0);

        List<Band> winners = new ArrayList<>();
        for (var entry : map.entrySet()) {
            if (entry.getValue() == maxVotes)
                winners.add(entry.getKey());
        }
        return winners;
    }
}
