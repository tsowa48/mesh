package mesh.page;

import mesh.DBManager;
import mesh.db.Loan;
import mesh.util;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/order", name = "Order")
public class Order extends HttpServlet {
    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mesh.db.User me = (mesh.db.User) request.getSession().getAttribute("me");
        if (me != null) {
            try {
                Integer oid = Integer.parseInt(request.getParameter("id"));
                EntityManager em = DBManager.getManager();
                em.clear();
                mesh.db.Order order = (mesh.db.Order) em.createQuery("select O from Order O where O.id = :oid")
                        .setParameter("oid", oid)
                        .getSingleResult();
                mesh.db.Client client = (mesh.db.Client) em.createQuery("select C from Client C where C.id = :cid")
                        .setParameter("cid", order.getCid())
                        .getSingleResult();
                List<mesh.db.Loan> loans = em.createQuery("select L from Loan L order by L.name asc").getResultList();

                order.setApproved(util.approveLoans(client, order, loans));

                request.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                request.setAttribute("order", order);
                request.setAttribute("client", client);
                request.getRequestDispatcher("WEB-INF/page/order.jsp").include(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
                response.sendRedirect("/");
            }
        } else {
            response.sendRedirect("/");
        }
    }
}
