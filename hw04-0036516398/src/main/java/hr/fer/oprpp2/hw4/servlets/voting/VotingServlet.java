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
import java.util.Collection;
import java.util.Map;

/**
 * Servlet used for showing voting page with all availible bands.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "Voting", urlPatterns = "/glasanje")
public class VotingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        Path bandsPath = Path.of(fileName);

        Map<Long, Band> bandsMap = VoteFileManager.getBands(bandsPath);
        Collection<Band> bands = bandsMap.values();

        req.setAttribute("bands", bands);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }
}
