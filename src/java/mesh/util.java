package mesh;

import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import mesh.db.Client;
import mesh.db.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author tsowa
 */
public class util {
  private static final sun.misc.BASE64Decoder b64Decoder = new sun.misc.BASE64Decoder();

  public static User tryLogin(String authString) {
    try {
      String[] authData = authString.split(" ");
      String authType = authData[0];//default=Basic
      if(!"Basic".equals(authType))
        return null;
      String[] credentials = new String(b64Decoder.decodeBuffer(authData[1])).split(":");
      Session session = HibernateUtil.getSession();
      User user = (User)session
              .createCriteria(User.class)
              .add(Restrictions.eq("login", credentials[0]))
              .add(Restrictions.eq("password", credentials[1]))
              .uniqueResult();
      //TODO: generate token
      return user;
    } catch(Exception ex) {
      ex.printStackTrace();//DEBUG
      return null;
    }
  }
  
  public static boolean isAuthorized(HttpServletRequest request) {
    User user = (User)request.getSession().getAttribute("user");
    if(user == null) {
      String authString = request.getHeader("Authorization");
      user = util.tryLogin(authString);
      if(user != null) {
        request.getSession().setAttribute("user", user);
        return true;
      }
    } else
      return true;
    return false;
  }
  
  public static Criteria detectField(Criteria in, String query) {
    Criterion crt = Restrictions.or(
            Restrictions.like("address", "%" + query + "%"),
            Restrictions.like("firstName", "%" + query + "%"),
            Restrictions.like("lastName", "%" + query + "%"),
            Restrictions.like("patronymic", "%" + query + "%"));
    if(containsInt(query))
      crt = Restrictions.or(
              Restrictions.eq("birth", Integer.parseInt(query)),
              Restrictions.like("doc.serial", "%" + query + "%"),
              Restrictions.like("doc.number", "%" + query + "%"),
              Restrictions.eq("doc.issued", Integer.parseInt(query))
      );
    if(query.contains(" ")) {
      String[] subQuery = query.split(" ");
      if(containsInt(query)) {
        crt = Restrictions.and(
                Restrictions.like("doc.serial", "%" + subQuery[0] + "%"),
                Restrictions.like("doc.number", "%" + subQuery[1] + "%")
        );
      } else {
        Criterion subCrt;
        if(subQuery.length > 2)
          subCrt = Restrictions.and(
                  Restrictions.like("firstName", "%" + subQuery[0] + "%"),
                  Restrictions.like("lastName", "%" + subQuery[1] + "%"),
                  Restrictions.like("patronymic", "%" + subQuery[2] + "%")
          );
        else if(subQuery.length > 1 && subQuery.length < 2)
          subCrt = Restrictions.and(
                  Restrictions.like("firstName", "%" + subQuery[0] + "%"),
                  Restrictions.like("lastName", "%" + subQuery[1] + "%")
          );
        else
          subCrt = Restrictions.like("firstName", "%" + subQuery[0] + "%");
        crt = Restrictions.or(
              Restrictions.like("address", "%" + query + "%"),
              subCrt
          );
      }
    }
    in.add(crt);
    return in;
  }
  
  private static boolean containsInt(String s) {
    return s.contains("0") || s.contains("1") || s.contains("2")
            || s.contains("3") || s.contains("4") || s.contains("5")
            || s.contains("6") || s.contains("7") || s.contains("8")
            || s.contains("9");
  }
  
  public static Double recountSolvency(Client c) {
    //TODO: recount coef by client info
    return new Random().nextDouble();//DEBUG
  }
}
