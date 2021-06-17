package hr.fer.oprpp2.hw6.blog.model;

import javax.persistence.*;
import java.util.List;

/**
 * Model, entity of User of blog.
 * User contains id that is automatically generated, firstName, lastName, nickname that is unique and called nick
 * , email and and a list of blog entries that user has created.
 * <p>
 * Entity user contains two named queries:
 * <code>BlogUser.getAll</code> that gets all users in database and
 * <code>BlogUser.getByNick</code> that gets user by nick provided in argument as nick.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
@NamedQueries({
        @NamedQuery(name = "BlogUser.getAll", query = "select u from BlogUser as u"),
        @NamedQuery(name = "BlogUser.getByNick", query = "select u from BlogUser as u where u.nick = :nick")
})
@Entity
@Table(name = "blog_users")
@Cacheable
public class BlogUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String nick;
    private String email;
    private byte[] passwordHash;
    private List<BlogEntry> blogEntryList;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(nullable = false, unique = true)
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Column(nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false, length = 1024)
    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    public List<BlogEntry> getBlogEntryList() {
        return blogEntryList;
    }

    public void setBlogEntryList(List<BlogEntry> blogEntryList) {
        this.blogEntryList = blogEntryList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogUser blogUser = (BlogUser) o;

        return id.equals(blogUser.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
