package hr.fer.oprpp2.hw6.blog.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * JPA Entity Manager Factory Provider.
 * Used for setting global, servlet side EntityManagerFactory instance.
 */
public class JPAEMFProvider {

    public static EntityManagerFactory emf;

    public static EntityManagerFactory getEmf() {
        return emf;
    }

    public static void setEmf(EntityManagerFactory emf) {
        JPAEMFProvider.emf = emf;
    }
}