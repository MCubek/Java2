package hr.fer.oprpp2.hw6.blog.model.form;

import hr.fer.oprpp2.hw6.blog.dao.DAOProvider;
import hr.fer.oprpp2.hw6.blog.model.BlogComment;
import hr.fer.oprpp2.hw6.blog.model.BlogEntry;
import hr.fer.oprpp2.hw6.blog.model.BlogUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper form model for {@link BlogComment}
 * Class is used for populating from {@link HttpServletRequest} or from original model,
 * for validation and populating into original model.
 * This class must be used when editing data models.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 03/06/2021
 */
public class BlogCommentForm {
    // Atributes from BlogComment
    private Long id;
    private BlogEntry blogEntry;
    private BlogUser user;
    private String message;
    private Date postedOn;

    /**
     * Errors map containing all errors in current form.
     * Populated when <code>validate()</code> is called.
     */
    Map<String, String> errors = new HashMap<>();

    /**
     * Method gets all errors as formatted string containg one error per line.
     * <code>validate()</code> must be called before errors are checked.
     *
     * @return formatted string as errors.
     */
    public String getErrors() {
        return String.join("\n", errors.values());
    }

    /**
     * Method gets all errors as formatted string containg one error per line.
     * <code>validate()</code> must be called before errors are checked.
     *
     * @return <code>true</code> if there are errors else <code>false</code>.
     */
    public boolean hasErrors() {
        return ! errors.isEmpty();
    }

    /**
     * Method populates form from {@link HttpServletRequest} with all available parameters.
     *
     * @param req http request
     */
    public void populateFromHttpRequest(HttpServletRequest req) {
        try {
            this.blogEntry = DAOProvider.getDAO().getBlogEntry(Long.valueOf(req.getParameter("entryId")));
        } catch (NumberFormatException ignored) {
        }
        try {
            this.user = DAOProvider.getDAO().getBlogUser((Long) req.getAttribute("currentID"));
        } catch (NumberFormatException ignored) {
        }
        this.message = req.getParameter("message");
    }

    /**
     * Method populates form from {@link BlogComment} with all its parameters.
     *
     * @param comment comment from which to populate
     */
    public void populateFromBlogComment(BlogComment comment) {
        this.id = comment.getId();
        this.blogEntry = comment.getBlogEntry();
        this.user = comment.getUser();
        this.message = comment.getMessage();
        this.postedOn = comment.getPostedOn();
    }

    /**
     * Method populates {@link BlogComment} from current form with all available parameters.
     *
     * @param comment comment to populate into
     */
    public void populateIntoBlogComment(BlogComment comment) {
        if (id != null) comment.setId(id);
        if (blogEntry != null) comment.setBlogEntry(blogEntry);
        if (user != null) comment.setUser(user);
        if (message != null && ! message.isBlank()) comment.setMessage(message);
        if (postedOn != null) comment.setPostedOn(postedOn);
    }

    /**
     * Method that validates current form and populates errors.
     */
    public void validate() {
        errors.clear();

        if (blogEntry == null) errors.put("blogEntry", "Blog Entry can't not exist!");
        if (message == null || message.isBlank()) errors.put("message", "Message can't be empty");
        if (postedOn == null) errors.put("postedOn", "Comment has to have a date.");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BlogEntry getBlogEntry() {
        return blogEntry;
    }

    public void setBlogEntry(BlogEntry blogEntry) {
        this.blogEntry = blogEntry;
    }

    public BlogUser getUser() {
        return user;
    }

    public void setUser(BlogUser user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Date postedOn) {
        this.postedOn = postedOn;
    }
}
