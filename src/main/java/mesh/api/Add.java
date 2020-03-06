package mesh.api;

import json.json;
import mesh.HibernateUtil;
import mesh.MeshResponse;
import mesh.db.Client;
import mesh.db.Loan;
import mesh.db.Order;
import mesh.db.User;
import mesh.util;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author tsowa
 */
@WebServlet("/add")
public class Add extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out = response.getWriter();
      try {
          if (util.isAuthorized(request)) {
              BufferedReader br = request.getReader();
              String data = br.lines().collect(Collectors.joining("\n"));
              json jData = new json(data);
              Session session = HibernateUtil.getSession();
              MeshResponse meshResponse = new MeshResponse(200);
              session.getTransaction().begin();
              System.out.println("------jData='" + jData.toString() + "'\n\n");//DEBUG
              if (data.contains("\"login\"") && data.contains("\"password\"")) {//User
                  User user = jData.toClass(User.class);
                  session.save(user);
                  meshResponse.setData(user);
              } else if (data.contains("desired_amount")) {//Order
                  Order order = jData.toClass(Order.class);
                  Client client = (Client) session
                          .createCriteria(Client.class)
                          .add(Restrictions.eq("id", order.cid))
                          .uniqueResult();
                  client.setSolvency(util.recountSolvency(client));
                  session.save(client);
                  List<Loan> loans = session
                          .createCriteria(Loan.class)
                          .list();
                  order.approved = util.approveLoans(client, loans);
                  session.save(order);
                  meshResponse.setData(order);
              } else if (data.contains("min_amount")) {//Loan
                  Loan loan = jData.toClass(Loan.class);
                  session.save(loan);
                  meshResponse.setData(loan);
              } else {//Client
                  Client client = jData.toClass(Client.class);
                  session.save(client);
                  meshResponse.setData(client);
              }
              session.getTransaction().commit();
              out.write(new json(meshResponse).toString());
          } else {
              response.setHeader("WWW-Authenticate", "Basic");
              out.write(new json(new MeshResponse(401)).toString());
              response.setStatus(401);
          }
      } catch (Exception ex) {
          ex.printStackTrace();//DEBUG
          out.write(new json(new MeshResponse(499)).toString());
      }
  }

}