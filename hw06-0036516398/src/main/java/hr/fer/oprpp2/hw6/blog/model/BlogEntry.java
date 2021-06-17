package hr.fer.oprpp2.hw6.blog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Model, entity foro blog entry.
 * Entry contains id, list of comments associated with it , date it was created, date it was
 * last modified at, a title, text and a creator.
 * <p>
 * Entity contains one named query called <code>BlogEntry.getByCreator</code> that retrieves all entries
 * that creator has created.
 */
@NamedQueries({
        @NamedQuery(name = "BlogEntry.getByCreator", query = "select e from BlogEntry as e where e.creator = :creator")
})
@Entity
@Table(name = "blog_entries")
@Cacheable()
public class BlogEntry {

    private Long id;
    private List<BlogComment> comments = new ArrayList<>();
    private Date createdAt;
    private Date lastModifiedAt;
    private String title;
    private String text;
    private BlogUser creator;
    private List<BlogUser> srca;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("postedOn")
    public List<BlogComment> getComments() {
        return comments;
    }

    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Column(length = 200, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 4096, nullable = false)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    public BlogUser getCreator() {
        return creator;
    }

    public void setCreator(BlogUser creator) {
        this.creator = creator;
    }

    @ManyToMany
    public List<BlogUser> getSrca() {
        return srca;
    }

    public void setSrca(List<BlogUser> srca) {
        this.srca = srca;
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
        BlogEntry other = (BlogEntry) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}