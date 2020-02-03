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
            Restrictions.ilike("address", "%" + query + "%"),
            Restrictions.ilike("firstName", "%" + query + "%"),
            Restrictions.ilike("lastName", "%" + query + "%"),
            Restrictions.ilike("patronymic", "%" + query + "%")
    );
    if(containsInt(query))
      crt = Restrictions.or(
              Restrictions.ilike("birth", "%" + query + "%"),
              Restrictions.ilike("doc.serial", "%" + query + "%"),
              Restrictions.ilike("doc.number", "%" + query + "%"),
              Restrictions.ilike("doc.issued", "%" + query + "%")
      );
    if(query.contains(" ")) {
      String[] subQuery = query.split(" ");
      if(containsInt(query)) {
        crt = Restrictions.and(
                Restrictions.ilike("doc.serial", "%" + subQuery[0] + "%"),
                Restrictions.ilike("doc.number", "%" + subQuery[1] + "%")
        );
      } else {
        Criterion subCrt;
        if(subQuery.length > 2)
          subCrt = Restrictions.and(
                  Restrictions.ilike("firstName", "%" + subQuery[0] + "%"),
                  Restrictions.ilike("lastName", "%" + subQuery[1] + "%"),
                  Restrictions.ilike("patronymic", "%" + subQuery[2] + "%")
          );
        else if(subQuery.length == 2)
          subCrt = Restrictions.and(
                  Restrictions.ilike("firstName", "%" + subQuery[0] + "%"),
                  Restrictions.ilike("lastName", "%" + subQuery[1] + "%")
          );
        else
          subCrt = Restrictions.ilike("firstName", "%" + subQuery[0] + "%");
        crt = Restrictions.or(
              Restrictions.ilike("address", "%" + query + "%"),
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
