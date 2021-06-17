package hr.fer.oprpp2.hw6.blog.web.servlets;

import com.google.gson.Gson;
import hr.fer.oprpp2.hw6.blog.dao.DAOProvider;
import hr.fer.oprpp2.hw6.blog.model.StatisticsEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 15/06/2021
 */
@WebServlet(name = "Statistics", value = "/servleti/statistics")
public class StatisticsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<StatisticsEntry> list = DAOProvider.getDAO().getBlogUsers().stream()
                .map(blogUser -> new StatisticsEntry(blogUser.getNick(), blogUser.getBlogEntryList().size()))
                .collect(Collectors.toList());

        StatisticsEntry[] array = new StatisticsEntry[list.size()];
        list.toArray(array);

        resp.setContentType("application/json;charset=UTF-8");

        Gson gson = new Gson();
        String jsonText = gson.toJson(array);

        resp.getWriter().write(jsonText);

        resp.getWriter().flush();
    }
}
