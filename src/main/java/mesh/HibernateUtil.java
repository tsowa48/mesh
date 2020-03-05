package mesh;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

/**
 *
 * @author tsowa
 */
public class HibernateUtil {

  private static final SessionFactory sessionFactory;
  private static Session session;
  
  static {
    try {
      sessionFactory = new Configuration().configure().buildSessionFactory();;
      if(session == null)
        session = sessionFactory.openSession();
    } catch (Throwable ex) {
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }
  
  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
  
  public static Session getSession() {
    if(session == null || !session.isOpen())
      session = sessionFactory.openSession();
    return session;
  }
}
