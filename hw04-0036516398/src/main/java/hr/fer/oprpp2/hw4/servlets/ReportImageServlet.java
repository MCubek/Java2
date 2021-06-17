package hr.fer.oprpp2.hw4.servlets;

import hr.fer.oprpp2.hw4.utils.Common;
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

/**
 * Servlet used for generating demo pi chart and sending it as a png.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "ReportImage", urlPatterns = "/reportImage")
public class ReportImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");

        OutputStream os = resp.getOutputStream();
        JFreeChart chart = generatePiChart();
        int height = 600;
        int width = 900;

        ChartUtils.writeChartAsPNG(os, chart, width, height);
        os.flush();
    }

    /**
     * Helper method for generating chart.
     *
     * @return Chart object with demo chart
     */
    private JFreeChart generatePiChart() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Windows", 75f);
        dataset.setValue("MacOS", 10f);
        dataset.setValue("Linux", 15f);

        String title = "OS Usage";

        return Common.generatePiChartFromDataset(title, dataset);
    }
}
