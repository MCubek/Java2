package hr.fer.oprpp2.hw4.servlets.voting;

import hr.fer.oprpp2.hw4.utils.Common;
import hr.fer.oprpp2.hw4.utils.voting.Band;
import hr.fer.oprpp2.hw4.utils.voting.VoteFileManager;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;

/**
 * Servlet for serving png with pi chart of
 * current votes cast for best band.
 * Servlet returns png file.
 * Servlet uses {@link JFreeChart} for creating pi chart.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "VotingChart", urlPatterns = "/glasanje-grafika")
public class VotingChart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bandsFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        String votesFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        Path bandsPath = Path.of(bandsFile);
        Path votesPath = Path.of(votesFile);

        var bandVotes = VoteFileManager.getSortedVotes(bandsPath, votesPath);

        resp.setContentType("image/png");

        OutputStream os = resp.getOutputStream();
        JFreeChart chart = generatePiChart(bandVotes);
        int height = 500;
        int width = 500;

        ChartUtils.writeChartAsPNG(os, chart, width, height);
        os.flush();
    }

    /**
     * Method for geting {@link JFreeChart} from map with bands and votes.
     *
     * @param bandVotes map with bands and votes
     * @return {@link JFreeChart} object with votes chart
     */
    private JFreeChart generatePiChart(Map<Band, Integer> bandVotes) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        String title = "Top bands";

        int voteSum = bandVotes.values().stream()
                .mapToInt(v -> v)
                .sum();

        for (var entry : bandVotes.entrySet()) {
            String name = entry.getKey().getName();
            double value = 1.0 * entry.getValue() / voteSum * 100;
            dataset.setValue(name, value);
        }

        return Common.generatePiChartFromDataset(title, dataset);
    }
}
