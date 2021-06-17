package hr.fer.oprpp2.hw4.servlets;

import hr.fer.oprpp2.hw4.Keys;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * Servlet for displaying dynamic html page with server uptime.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "AppInfo", urlPatterns = "/appinfo.jsp")
public class AppInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instant start = (Instant) req.getServletContext().getAttribute(Keys.KEY_START_TIME);

        Duration duration = Duration.between(start, Instant.now());
        String timeFormatted = String.format("%d days %d hours %d minutes %d seconds and %d milliseconds",
                duration.toDays(),
                duration.toHoursPart(),
                duration.toMinutesPart(),
                duration.toSecondsPart(),
                duration.toMillisPart());

        req.setAttribute("since_start", timeFormatted);

        req.getRequestDispatcher("/WEB-INF/pages/appinfo.jsp").forward(req, resp);
    }
}
