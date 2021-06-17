package hr.fer.oprpp2.hw5.servlets.polls;

import hr.fer.oprpp2.hw5.dao.DAOProvider;
import hr.fer.oprpp2.hw5.model.PollOption;
import hr.fer.oprpp2.hw5.utils.Common;
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
import java.util.List;

import static hr.fer.oprpp2.hw5.utils.Common.sendErrorMessage;

/**
 * Servlet for serving png with pi chart of
 * current votes cast for best candidate.
 * Servlet returns png file.
 * Servlet uses {@link JFreeChart} for creating pi chart.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "VotingChart", urlPatterns = "/servleti/glasanje-grafika")
public class VotingChart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long pollID;
        try {
            pollID = Long.parseLong(req.getParameter("pollID"));
        } catch (NumberFormatException exception) {
            sendErrorMessage(req, resp, "Invalid poll selected!");
            return;
        }

        resp.setContentType("image/png");

        List<PollOption> candidates = DAOProvider.getDao().getPollOptionsForPoll(pollID);

        OutputStream os = resp.getOutputStream();
        JFreeChart chart = generatePiChart(candidates);
        int height = 500;
        int width = 500;

        ChartUtils.writeChartAsPNG(os, chart, width, height);
        os.flush();
    }

    /**
     * Method for geting {@link JFreeChart} from map with candidates and votes.
     *
     * @param candidates map with bands and votes
     * @return {@link JFreeChart} object with votes chart
     */
    private JFreeChart generatePiChart(List<PollOption> candidates) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        String title = "Top candidates:";

        long voteSum = candidates.stream()
                .mapToLong(PollOption::getVotesCount)
                .sum();

        for (var entry : candidates) {
            String name = entry.getOptionTitle();
            double value = 1.0 * entry.getVotesCount() / voteSum * 100;
            dataset.setValue(name, value);
        }

        return Common.generatePiChartFromDataset(title, dataset);
    }
}
