package hr.fer.oprpp2.hw6.blog.dao;

import hr.fer.oprpp2.hw6.blog.dao.jpa.JPADAOImpl;

/**
 * DAO Provider helper class for servlets to use when they
 * need access to DAO methods.
 */
public class DAOProvider {

    private static final DAO dao = new JPADAOImpl();

    public static DAO getDAO() {
        return dao;
    }

}