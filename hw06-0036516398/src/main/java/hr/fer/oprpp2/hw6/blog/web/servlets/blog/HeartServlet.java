package hr.fer.oprpp2.hw6.blog.web.servlets.blog;

import hr.fer.oprpp2.hw6.blog.dao.DAOProvider;
import hr.fer.oprpp2.hw6.blog.model.BlogEntry;
import hr.fer.oprpp2.hw6.blog.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static hr.fer.oprpp2.hw6.blog.utils.Common.sendErrorMessage;

/**
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 15/06/2021
 */
@WebServlet(name = "Heart", value = "/servleti/heartPost/*")
public class HeartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path == null) {
            sendErrorMessage(req, resp, "URL Invalid");
            return;
        }

        //Check path is valid
        if (path.startsWith("/")) path = path.substring(1);

        long postId;
        try{
            postId = Long.parseLong(path);
        }catch (NumberFormatException e){
            sendErrorMessage(req, resp, "URL Invalid");
            return;
        }

        BlogEntry entry= DAOProvider.getDAO().getBlogEntry(postId);

        if(entry==null){
            sendErrorMessage(req, resp, "Invalid");
            return;
        }

        BlogUser currentUser = DAOProvider.getDAO().getBlogUser((Long) req.getAttribute("currentID"));
        if(currentUser==null){
            sendErrorMessage(req, resp, "Must be logged in");
            return;
        }

        var listSrca = entry.getSrca();

        if(!listSrca.contains(currentUser))
            listSrca.add(currentUser);

        resp.sendRedirect(req.getContextPath() + "/servleti/author/"+entry.getCreator().getNick()+"/"+entry.getId());
    }
}
