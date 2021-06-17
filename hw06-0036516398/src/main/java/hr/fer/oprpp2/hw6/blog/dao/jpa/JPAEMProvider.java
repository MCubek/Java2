package hr.fer.oprpp2.hw6.blog.dao.jpa;

import hr.fer.oprpp2.hw6.blog.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * JPA Entity Manager provider.
 * Used by DAO to get Entity manager for queries.
 */
public class JPAEMProvider {

    private static final ThreadLocal<EntityManager> locals = new ThreadLocal<>();

    public static EntityManager getEntityManager() {
        EntityManager em = locals.get();
        if (em == null) {
            em = JPAEMFProvider.getEmf().createEntityManager();
            em.getTransaction().begin();
            locals.set(em);
        }
        return em;
    }

    public static void close() throws DAOException {
        EntityManager em = locals.get();
        if (em == null) {
            return;
        }
        DAOException dex = null;
        try {
            em.getTransaction().commit();
        } catch (Exception ex) {
            dex = new DAOException("Unable to commit transaction.", ex);
        }
        try {
            em.close();
        } catch (Exception ex) {
            if (dex != null) {
                dex = new DAOException("Unable to close entity manager.", ex);
            }
        }
        locals.remove();
        if (dex != null) throw dex;
    }

}