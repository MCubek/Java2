package hr.fer.oprpp2.hw6.blog.model.form;

import hr.fer.oprpp2.hw6.blog.model.BlogUser;
import hr.fer.oprpp2.hw6.blog.utils.Crypto;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper form model for {@link BlogUser}
 * Class is used for populating from {@link HttpServletRequest} or from original model,
 * for validation and populating into original model.
 * This class must be used when editing data models.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
public class BlogUserForm {
    // Atributes from BlogComment
    private Long id;
    private String firstName;
    private String lastName;
    private String nick;
    private String email;
    private byte[] passwordHash;

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
        this.firstName = req.getParameter("fname").trim();
        this.lastName = req.getParameter("lname").trim();
        this.email = req.getParameter("email").trim();
        this.nick = req.getParameter("nick").trim();
        String tempPassword = req.getParameter("password");
        this.passwordHash = tempPassword != null ? Crypto.calculateDigest(tempPassword.trim()) : null;
    }

    /**
     * Method populates form from {@link BlogUser} with all its parameters.
     *
     * @param blogUser user from which to populate
     */
    public void populateFromBlogUser(BlogUser blogUser) {
        this.id = blogUser.getId();
        this.firstName = blogUser.getFirstName();
        this.lastName = blogUser.getLastName();
        this.nick = blogUser.getNick();
        this.email = blogUser.getEmail();
        this.passwordHash = blogUser.getPasswordHash();
    }

    /**
     * Method populates {@link BlogUser} from current form with all available parameters.
     *
     * @param blogUser user to populate into
     */
    public void populateIntoBlogUser(BlogUser blogUser) {
        if (this.id != null) blogUser.setId(this.id);
        if (this.firstName != null && ! this.firstName.isBlank()) blogUser.setFirstName(this.firstName);
        if (this.lastName != null && ! this.lastName.isBlank()) blogUser.setLastName(this.lastName);
        if (this.email != null && ! this.email.isBlank()) blogUser.setEmail(this.email);
        if (this.nick != null && ! this.nick.isBlank()) blogUser.setNick(this.nick);
        if (this.passwordHash != null) blogUser.setPasswordHash(this.passwordHash);
    }

    /**
     * Method that validates current form and populates errors.
     */
    public void validate() {
        errors.clear();

        if (firstName.isBlank())
            errors.put("firstName", "First name is required!");
        if (lastName.isBlank())
            errors.put("lastName", "Last name is required!");
        if (nick.isBlank())
            errors.put("nick", "Nick is required!");
        if (passwordHash == null)
            errors.put("password", "Password hash is empty!");

        if (this.email.isBlank()) {
            errors.put("email", "Email is required!");
        } else {
            int l = email.length();
            int p = email.indexOf('@');
            if (l < 3 || p == - 1 || p == 0 || p == l - 1) {
                errors.put("email", "Email invalid format.");
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }
}
