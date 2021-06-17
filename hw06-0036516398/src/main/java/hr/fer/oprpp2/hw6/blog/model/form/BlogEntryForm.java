package hr.fer.oprpp2.hw6.blog.model.form;

import hr.fer.oprpp2.hw6.blog.dao.DAOProvider;
import hr.fer.oprpp2.hw6.blog.model.BlogEntry;
import hr.fer.oprpp2.hw6.blog.model.BlogUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper form model for {@link BlogEntry}
 * Class is used for populating from {@link HttpServletRequest} or from original model,
 * for validation and populating into original model.
 * This class must be used when editing data models.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 03/06/2021
 */
public class BlogEntryForm {
    // Atributes from BlogComment
    private Long id;
    private Date createdAt;
    private Date lastModifiedAt;
    private String title;
    private String text;
    private BlogUser creator;

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
        String id = req.getParameter("entryId");
        if (id != null && ! id.isBlank()) {
            try {
                this.id = Long.parseLong(id);
            } catch (NumberFormatException ignore) {
            }
        }
        this.title = req.getParameter("entryTitle");
        this.text = req.getParameter("entryText");
        try {
            this.creator = DAOProvider.getDAO().getBlogUser((Long) req.getAttribute("currentID"));
        } catch (NumberFormatException ignored) {
        }
    }

    /**
     * Method populates form from {@link BlogEntry} with all its parameters.
     *
     * @param blogEntry entry from which to populate
     */
    public void populateFromBlogEntry(BlogEntry blogEntry) {
        id = blogEntry.getId();
        createdAt = blogEntry.getCreatedAt();
        lastModifiedAt = blogEntry.getLastModifiedAt();
        title = blogEntry.getTitle();
        text = blogEntry.getText();
        creator = blogEntry.getCreator();
    }

    /**
     * Method populates {@link BlogEntry} from current form with all available parameters.
     *
     * @param blogEntry comment to populate into
     */
    public void populateIntoBlogEntry(BlogEntry blogEntry) {
        if (id != null) blogEntry.setId(id);
        if (createdAt != null) blogEntry.setCreatedAt(createdAt);
        if (lastModifiedAt != null) blogEntry.setLastModifiedAt(lastModifiedAt);
        if (title != null && ! title.isBlank()) blogEntry.setTitle(title);
        if (text != null && ! text.isBlank()) blogEntry.setText(text);
        if (creator != null) blogEntry.setCreator(creator);
    }

    /**
     * Method that validates current form and populates errors.
     */
    public void validate() {
        errors.clear();

        if (createdAt == null) errors.put("createdAt", "Created at date not set");
        if (lastModifiedAt == null) errors.put("lastModifiedAt", "Last modified at date not set");
        if (title == null || title.isBlank()) errors.put("title", "Title empty");
        if (text == null || text.isBlank()) errors.put("text", "Text empty");
        if (creator == null) errors.put("creator", "Creator unknown");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BlogUser getCreator() {
        return creator;
    }

    public void setCreator(BlogUser creator) {
        this.creator = creator;
    }
}
