package hr.fer.oprpp2.hw6.blog.web.servlets.blog;

import hr.fer.oprpp2.hw6.blog.dao.DAOProvider;
import hr.fer.oprpp2.hw6.blog.model.BlogEntry;
import hr.fer.oprpp2.hw6.blog.model.BlogUser;
import hr.fer.oprpp2.hw6.blog.model.form.BlogEntryForm;
import hr.fer.oprpp2.hw6.blog.utils.Common;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static hr.fer.oprpp2.hw6.blog.utils.Common.sendErrorMessage;

/**
 * Servlet assigned to handle everything around blog entries.
 * Servlet can show entries for given author, show entry with all its comments,
 * add and edit existing entries.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
@WebServlet(name = "BlogEntries", urlPatterns = "/servleti/author/*")
public class BlogEntriesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path == null) {
            sendErrorMessage(req, resp, "URL Invalid");
            return;
        }

        //Check path is valid
        if (path.startsWith("/")) path = path.substring(1);
        String[] split = path.split("/");
        if (split.length > 2) {
            sendErrorMessage(req, resp, "To many arguments in url");
            return;
        }

        //Split path arguments and check if user exists with given nickname
        String nick = split[0];
        BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);
        if (user == null) {
            sendErrorMessage(req, resp, "No such author");
            return;
        }

        if (split.length == 1) {
            //Only nickname
            serveListRequest(req, resp, user);
        } else {
            //Nickname and another argument
            switch (split[1].toLowerCase()) {
                case "new" -> serveNewEntryRequest(req, resp, user);
                case "edit" -> serveEditEntryRequest(req, resp, user);
                default -> {
                    try {
                        serveShowPageRequest(req, resp, user, Long.valueOf(split[1]));
                    } catch (NumberFormatException e) {
                        sendErrorMessage(req, resp, "Invalid entry id");
                    }
                }
            }
        }

    }

    /**
     * Method that serves list of entries for given user using a jsp page for GET request.
     *
     * @param req  request
     * @param resp response
     * @param user user who's entries to serve
     * @throws ServletException when error occurs
     * @throws IOException      when error occurs
     */
    private void serveListRequest(HttpServletRequest req, HttpServletResponse resp, BlogUser user) throws ServletException, IOException {
        req.setAttribute("authorId", user.getId());
        req.setAttribute("authorNick", user.getNick());
        req.setAttribute("blogEntries", user.getBlogEntryList());

        req.getRequestDispatcher("/WEB-INF/pages/BlogEntries.jsp").forward(req, resp);
    }

    /**
     * Method that serves user's entry page for GET method.
     *
     * @param req     request
     * @param resp    response
     * @param user    user who's page is being served
     * @param entryId entry id of user's page that will be serverd
     * @throws ServletException when error occurs
     * @throws IOException      when error occurs
     */
    private void serveShowPageRequest(HttpServletRequest req, HttpServletResponse resp, BlogUser user, Long entryId) throws ServletException, IOException {
        req.setAttribute("authorId", user.getId());
        req.setAttribute("authorNick", user.getNick());

        //Retrieve entry and check if it exists and user created it.
        BlogEntry entry = DAOProvider.getDAO().getBlogEntry(entryId);
        if (entry == null || ! entry.getCreator().equals(user)) {
            Common.sendErrorMessage(req, resp, "User doesn't match entry!");
            return;
        }

        req.setAttribute("brojSrca",entry.getSrca().size());
        req.setAttribute("blog", entry);
        req.setAttribute("comments", entry.getComments());

        req.getRequestDispatcher("/WEB-INF/pages/BlogEntry.jsp").forward(req, resp);
    }

    /**
     * Method that serves new entry form page for GET method.
     *
     * @param req  request
     * @param resp response
     * @param user user who's new page will be created
     * @throws ServletException when error occurs
     * @throws IOException      when error occurs
     */
    private void serveNewEntryRequest(HttpServletRequest req, HttpServletResponse resp, BlogUser user) throws ServletException, IOException {
        //Check if user who wants to create the page is logged in as that user
        if (! user.getId().equals(req.getAttribute("currentID"))) {
            Common.sendErrorMessage(req, resp, "Current user not permitted to add entry!");
            return;
        }

        req.setAttribute("errorMessage", "");
        req.setAttribute("entryForm", new BlogEntryForm());

        req.getRequestDispatcher("/WEB-INF/pages/AddBlogEntry.jsp").forward(req, resp);
    }

    /**
     * Method that serves edit entry form page for GET Method.
     *
     * @param req  request
     * @param resp response
     * @param user user who's page will be edited.
     * @throws ServletException when error occurs
     * @throws IOException      when error occurs
     */
    private void serveEditEntryRequest(HttpServletRequest req, HttpServletResponse resp, BlogUser user) throws ServletException, IOException {
        //Check if user who wants to edit the page is logged in as that user
        if (! user.getId().equals(req.getAttribute("currentID"))) {
            Common.sendErrorMessage(req, resp, "Current user not permitted to edit entry!");
            return;
        }

        //Retrieve id of page that is going to be edited.
        String entryIdString = req.getParameter("entryId");
        if (entryIdString == null || entryIdString.isBlank()) {
            Common.sendErrorMessage(req, resp, "Entry id is not provided!");
            return;
        }

        //Parse entry id
        long entryId;
        try {
            entryId = Long.parseLong(entryIdString);
        } catch (NumberFormatException e) {
            Common.sendErrorMessage(req, resp, "Entry id is invalid!");
            return;
        }

        //Retrieve entry with id from DAO
        BlogEntry entry = DAOProvider.getDAO().getBlogEntry(entryId);
        if (entry == null || ! entry.getCreator().equals(user)) {
            Common.sendErrorMessage(req, resp, "Entry not accessible!");
            return;
        }

        //Populate form model that will be used in jsp
        BlogEntryForm form = new BlogEntryForm();
        form.populateFromBlogEntry(entry);

        req.setAttribute("errorMessage", "");
        req.setAttribute("entryForm", form);

        req.getRequestDispatcher("/WEB-INF/pages/EditBlogEntry.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            sendErrorMessage(req, resp, "URL Invalid");
            return;
        }
        //Check path is valid
        if (path.startsWith("/")) path = path.substring(1);
        String[] split = path.split("/");
        if (split.length != 2) {
            sendErrorMessage(req, resp, "Invalid number of arguments!");
            return;
        }

        //Split path and retrieve user from DAO
        String nick = split[0];
        BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);
        if (user == null) {
            sendErrorMessage(req, resp, "No such author");
            return;
        }
        //Check if given id matches one from session
        if (! user.getId().equals(req.getAttribute("currentID"))) {
            Common.sendErrorMessage(req, resp, "Current user not permitted!");
            return;
        }

        //Populate form from request
        BlogEntryForm entryForm = new BlogEntryForm();
        entryForm.populateFromHttpRequest(req);

        switch (split[1].toLowerCase()) {
            case "new" -> serveNewEntryPost(req, resp, user, entryForm);
            case "edit" -> serveEditEntryPost(req, resp, user, entryForm);
            default -> {
                Common.sendErrorMessage(req, resp, "Invalid argument!");
            }
        }
    }

    /**
     * Method that posts edit on blog entry.
     *
     * @param req       request
     * @param resp      response
     * @param user      user that edited the entry and the entry's owner
     * @param entryForm entry form from POST params
     * @throws ServletException when error occurs
     * @throws IOException      when error occurs
     */
    private void serveEditEntryPost(HttpServletRequest req, HttpServletResponse resp, BlogUser user, BlogEntryForm entryForm) throws ServletException, IOException {
        //Get original entry from dao
        BlogEntry originalEntry = DAOProvider.getDAO().getBlogEntry(entryForm.getId());
        //Check if entry exists
        if (originalEntry == null || ! originalEntry.getCreator().equals(user)) {
            Common.sendErrorMessage(req, resp, "User is not the owner of entry!");
            return;
        }

        //Set missing values from original entry and current time
        entryForm.setCreatedAt(originalEntry.getCreatedAt());
        entryForm.setLastModifiedAt(new Date());

        entryForm.validate();
        if (entryForm.hasErrors()) {
            req.setAttribute("errorMessage", entryForm.getErrors());
            req.setAttribute("entryForm", entryForm);

            req.getRequestDispatcher("/WEB-INF/pages/EditBlogEntry.jsp").forward(req, resp);
            return;
        }

        //Fill original entry and persist it
        entryForm.populateIntoBlogEntry(originalEntry);
        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick() + "/" + originalEntry.getId());
    }

    /**
     * Method that posts new blog entry.
     *
     * @param req       request
     * @param resp      response
     * @param user      user that is posting the page
     * @param entryForm entry form from POST params
     * @throws ServletException when error occurs
     * @throws IOException      when error occurs
     */
    private void serveNewEntryPost(HttpServletRequest req, HttpServletResponse resp, BlogUser user, BlogEntryForm entryForm) throws ServletException, IOException {
        //Populate missing items into form
        var currentDate = new Date();
        entryForm.setCreatedAt(currentDate);
        entryForm.setLastModifiedAt(currentDate);

        entryForm.validate();
        if (entryForm.hasErrors()) {
            req.setAttribute("errorMessage", entryForm.getErrors());
            req.setAttribute("entryForm", entryForm);

            req.getRequestDispatcher("/WEB-INF/pages/AddBlogEntry.jsp").forward(req, resp);
            return;
        }
        //Create entry, populate it from form and persist it.
        BlogEntry entry = new BlogEntry();
        entryForm.populateIntoBlogEntry(entry);
        DAOProvider.getDAO().persistBlogEntry(entry);

        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick() + "/" + entry.getId());
    }
}
