package mesh.api;

import json.json;
import mesh.DBManager;
import mesh.MeshResponse;
import mesh.util;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@WebServlet(value = "/api/order", name = "Order api")
public class Order extends HttpServlet {

    @POST
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        mesh.db.User me = (mesh.db.User) request.getSession().getAttribute("me");
        MeshResponse meshResponse = new MeshResponse(200);
        if (me != null) {
            EntityManager em = DBManager.getManager();
            json jData = new json(request.getReader().lines().collect(Collectors.joining(" ")));
            Integer cid = Integer.parseInt(jData.get("cid"));
            Double wish_summ = Double.parseDouble(jData.get("wish_summ"));
            Integer wish_date = Integer.parseInt(jData.get("wish_date"));
            Integer order_date = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));

            mesh.db.Client client = (mesh.db.Client) em
                    .createNativeQuery("select C.* from client C, document D where D.type=0 and D.cid=C.id and C.id=:cid", mesh.db.Client.class)
                    .setParameter("cid", cid)
                    .getSingleResult();
            Double newSolvency = util.recountSolvency(client);
            em.getTransaction().begin();
            em.createNativeQuery("update client set solvency=:solvency where id=:cid")
                    .setParameter("solvency", newSolvency)
                    .setParameter("cid", client.getId())
                    .executeUpdate();
            em.getTransaction().commit();
            mesh.db.Order order = (mesh.db.Order) em
                    .createNativeQuery("insert into orders(uid, cid, desired_amount, desired_term, date) values(:uid, :cid, :desired_amount, :desired_term, :date) returning *", mesh.db.Order.class)
                    .setParameter("uid", me.getId())
                    .setParameter("cid", cid)
                    .setParameter("desired_amount", wish_summ)
                    .setParameter("desired_term", wish_date)
                    .setParameter("date", order_date)
                    .getSingleResult();
            meshResponse.setData(order);
        }
        out.write(new json(meshResponse).toString());
    }
}
