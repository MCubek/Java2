package hr.fer.oprpp2.hw6.blog.dao;

import hr.fer.oprpp2.hw6.blog.model.BlogComment;
import hr.fer.oprpp2.hw6.blog.model.BlogEntry;
import hr.fer.oprpp2.hw6.blog.model.BlogUser;

import java.util.List;

/**
 * DAO interface for providing operations upon stored data.
 */
public interface DAO {

    /**
     * Retreives BlogEntry with given <code>id</code>. If one doesn't exist
     * returns <code>null</code>.
     *
     * @param id primary key, id of BlogEntry
     * @return entry or <code>null</code> if entry doesn't exist
     * @throws DAOException if error occurs while retrieving
     */
    BlogEntry getBlogEntry(Long id) throws DAOException;

    /**
     * Retrieves BlogEntries with creator in argument. If there are none method
     * returns empty list.
     *
     * @param creator creator of blogentry
     * @return list of blog entries for given creator
     * @throws DAOException if error occurs while retrieving
     */
    List<BlogEntry> getBlogEntriesByUser(BlogUser creator) throws DAOException;

    /**
     * Retreives BlogUser with given <code>id</code>. If one doesn't exist
     * returns <code>null</code>.
     *
     * @param id primary key, id of BlogUser
     * @return user or <code>null</code> if user doesn't exist
     * @throws DAOException if error occurs while retrieving
     */
    BlogUser getBlogUser(Long id) throws DAOException;

    /**
     * Retrieves BlogUser with given <code>nick</code>. If one doesn't exist
     * return <code>null</code>.
     * Nick is a unique identifier of BlogUser.
     *
     * @param nick nickname of BlogUser
     * @return user or <code>null</code> if user doesn't exist
     * @throws DAOException if error occurs while retrieving
     */
    BlogUser getBlogUserByNick(String nick) throws DAOException;

    /**
     * Retreives All registered users and returns them as a list.
     * If none exist method return empty list.
     *
     * @return list with users or empty list if there are none
     * @throws DAOException if error occurs while retrieving
     */
    List<BlogUser> getBlogUsers() throws DAOException;

    /**
     * Persist blogUser into database.
     *
     * @param blogUser user to persist
     * @throws DAOException if error occurs while saving
     */
    void persistBlogUser(BlogUser blogUser) throws DAOException;


    /**
     * Method for persisting comment into database.
     *
     * @param blogComment comment to persist
     * @throws DAOException if error occurs while saving
     */
    void persistBlogComment(BlogComment blogComment) throws DAOException;

    /**
     * Method for persisting entry into database.
     *
     * @param entry entry to persist
     * @throws DAOException if error occurs while saving
     */
    void persistBlogEntry(BlogEntry entry) throws DAOException;
}