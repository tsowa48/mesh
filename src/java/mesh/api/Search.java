package mesh.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.json;
import mesh.*;
import mesh.db.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
/**
 *
 * @author tsowa
 */
public class Search extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("application/json;charset=utf-8");
    PrintWriter out = response.getWriter();
    try {
      if(util.isAuthorized(request)) {
        MeshResponse meshResponse;
        String query = request.getParameter("q");
        meshResponse = new MeshResponse(200);
        Session session = HibernateUtil.getSession();
        Criteria crt = session.createCriteria(Client.class, "client")
                .createAlias("client.documents", "doc");
        if(query == null || query.isEmpty()) {
          crt = crt.createAlias("client.orders", "order");
          User user = (User)request.getSession().getAttribute("user");
          crt.add(Restrictions.eq("order.uid", user.id));
        } else {
          crt = util.detectField(crt, query.replaceAll("%20", " ").toLowerCase());
        }
        crt.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<Client> clients = crt.list();
        //TODO: recount client solvency on create new order
        //clients.forEach(it -> it.solvency = util.recountSolvency(it));
        meshResponse.setData(clients.toArray());
        out.write(new json(meshResponse).toString());
      } else {
        response.setHeader("WWW-Authenticate", "Basic realm=\"api.mesh\"");
        out.write(new json(new MeshResponse(401)).toString());
        response.setStatus(401);
      }
    } catch(Exception ex) {
      ex.printStackTrace();//DEBUG
      out.write(new json(new MeshResponse(499)).toString());
    }
  }

}
