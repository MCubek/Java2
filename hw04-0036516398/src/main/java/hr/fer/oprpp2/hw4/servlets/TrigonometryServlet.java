package hr.fer.oprpp2.hw4.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for displaying dynamic html page of trigonometry values.
 * Servlet requires two arguments a and b that it validates and sets to default
 * values 0 and 360 respectively if invalid.
 * Servlet calculates and redirects values to dynamic jsp.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "Trigonometry", urlPatterns = "/trigonometric")
public class TrigonometryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int a, b;

        //Parsing and validating values
        try {
            a = Integer.parseInt(req.getParameter("a"));
        } catch (NumberFormatException e) {
            a = 0;
        }
        try {
            b = Integer.parseInt(req.getParameter("b"));
        } catch (NumberFormatException e) {
            b = 360;
        }
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        } else if (b > a + 720) {
            b = a + 720;
        }

        //Calculating results
        List<Integer> angles = new ArrayList<>();
        List<Double> sines = new ArrayList<>();
        List<Double> cosines = new ArrayList<>();
        for (int i = a; i <= b; i++) {
            angles.add(i);
            sines.add(Math.sin(Math.toRadians(i)));
            cosines.add(Math.cos(Math.toRadians(i)));
        }
        req.setAttribute("angles", angles);
        req.setAttribute("sines", sines);
        req.setAttribute("cosines", cosines);

        req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
    }
}
