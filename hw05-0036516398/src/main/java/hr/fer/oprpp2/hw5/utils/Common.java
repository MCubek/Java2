package hr.fer.oprpp2.hw5.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * Class containing common static methods for all servlets to use.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 09/04/2021
 */
public class Common {
    private Common() {
    }

    /**
     * Method for generating Pie chart from Pie dataset and title.
     *
     * @param title   title of chart
     * @param dataset dataset to create chart from
     * @return Pie chart object
     */
    public static JFreeChart generatePiChartFromDataset(String title, PieDataset<?> dataset) {
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, false, false, false);

        chart.setBorderPaint(Color.BLACK);
        chart.setBorderStroke(new BasicStroke(2f));
        chart.setBorderVisible(true);

        return chart;
    }

    /**
     * Static method for sending custom error message to client
     *
     * @param req request
     * @param resp response
     * @param message message to send
     * @throws ServletException servlet error
     * @throws IOException io error
     */
    public static void sendErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("MESSAGE", message);
        req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
    }
}
