package hr.fer.oprpp2.hw4.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;

import java.awt.*;

/**
 * Class containing common static methods for all servlets to use.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
public class Common {
    private Common() {
    }

    /**
     * Method for generating Pie chart from Pie dataset and title.
     *
     * @param title title of chart
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
}
