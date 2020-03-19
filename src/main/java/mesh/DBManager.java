package mesh;

import javax.persistence.*;

public final class DBManager {
    private static EntityManagerFactory entityManagerFactory = null;
    private static EntityManager em;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("mesh");
            em = entityManagerFactory.createEntityManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static EntityManager getManager() {
        if(em == null || !em.isOpen())
            em = entityManagerFactory.createEntityManager();
        return em;
    }
}
