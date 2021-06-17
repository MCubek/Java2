package hr.fer.oprpp2.hw6.blog.web.servlets.blog;

import hr.fer.oprpp2.hw6.blog.dao.DAOProvider;
import hr.fer.oprpp2.hw6.blog.model.BlogComment;
import hr.fer.oprpp2.hw6.blog.model.form.BlogCommentForm;
import hr.fer.oprpp2.hw6.blog.utils.Common;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet for posting comments.
 * Servlet accepts POST requests.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 03/06/2021
 */
@WebServlet(name = "addComment", urlPatterns = "/servleti/addComment")
public class CommentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Populate form from request and add missing items
        BlogCommentForm blogCommentForm = new BlogCommentForm();
        blogCommentForm.populateFromHttpRequest(req);
        blogCommentForm.setPostedOn(new Date());

        blogCommentForm.validate();
        if (blogCommentForm.hasErrors()) {
            Common.sendErrorMessage(req, resp, blogCommentForm.getErrors());
            return;
        }

        //Create comment and populate it from form
        BlogComment comment = new BlogComment();
        blogCommentForm.populateIntoBlogComment(comment);

        DAOProvider.getDAO().persistBlogComment(comment);

        resp.sendRedirect(req.getContextPath() + "/servleti/author/"
                + req.getParameter("authorNick") + "/" + req.getParameter("entryId"));
    }

}
