package hr.fer.oprpp2.hw6.blog.dao.jpa;

import hr.fer.oprpp2.hw6.blog.dao.DAO;
import hr.fer.oprpp2.hw6.blog.dao.DAOException;
import hr.fer.oprpp2.hw6.blog.model.BlogComment;
import hr.fer.oprpp2.hw6.blog.model.BlogEntry;
import hr.fer.oprpp2.hw6.blog.model.BlogUser;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * {@link DAO} interface Implementation using JPA.
 */
public class JPADAOImpl implements DAO {

    @Override
    public BlogEntry getBlogEntry(Long id) throws DAOException {
        return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
    }

    @Override
    public List<BlogEntry> getBlogEntriesByUser(BlogUser creator) throws DAOException {
        var query = JPAEMProvider.getEntityManager().createNamedQuery("BlogEntry.getByCreator", BlogEntry.class);
        query.setParameter("creator", creator);
        return query.getResultList();
    }

    @Override
    public BlogUser getBlogUser(Long id) throws DAOException {
        return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
    }

    @Override
    public BlogUser getBlogUserByNick(String nick) throws DAOException {
        var query = JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getByNick", BlogUser.class);
        query.setParameter("nick", nick);
        BlogUser blogUser = null;
        try {
            blogUser = query.getSingleResult();
        } catch (NoResultException ignore) {
        }
        return blogUser;
    }

    @Override
    public List<BlogUser> getBlogUsers() throws DAOException {
        return JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getAll", BlogUser.class).getResultList();
    }

    @Override
    public void persistBlogUser(BlogUser blogUser) throws DAOException {
        JPAEMProvider.getEntityManager().persist(blogUser);
    }

    @Override
    public void persistBlogComment(BlogComment blogComment) throws DAOException {
        JPAEMProvider.getEntityManager().persist(blogComment);
    }

    @Override
    public void persistBlogEntry(BlogEntry entry) throws DAOException {
        JPAEMProvider.getEntityManager().persist(entry);
    }
}