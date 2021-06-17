package hr.fer.oprpp2.hw6.blog.model;

import java.util.Date;

import javax.persistence.*;

/**
 * Model, entity for blog comment.
 * Comment contains id that is automatically generated, blogEntry that it belongs to,
 * user that made it, message and date it was posted.
 * <p>
 * Entity contains one named query called <code>BlogComment.getCommentsForBlogEntry</code> that
 * returns all comments as list for blogEntry called blogEntry it belongs to.
 */
@NamedQueries({
        @NamedQuery(name = "BlogComment.getCommentsForBlogEntry", query = "select c from BlogComment as c where c.blogEntry = :blogEntry ")
})
@Entity
@Table(name = "blog_comments")
public class BlogComment {

    private Long id;
    private BlogEntry blogEntry;
    private BlogUser user;
    private String message;
    private Date postedOn;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    public BlogEntry getBlogEntry() {
        return blogEntry;
    }

    public void setBlogEntry(BlogEntry blogEntry) {
        this.blogEntry = blogEntry;
    }

    @ManyToOne
    public BlogUser getUser() {
        return user;
    }

    public void setUser(BlogUser user) {
        this.user = user;
    }

    @Column(length = 4096, nullable = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Date postedOn) {
        this.postedOn = postedOn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlogComment other = (BlogComment) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}