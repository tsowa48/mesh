package mesh.api;

import json.*;
import mesh.DBManager;
import mesh.MeshResponse;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

@WebServlet(value = "/api/client", name = "Client")
public class Client extends HttpServlet {

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        mesh.db.User user = (mesh.db.User)request.getSession().getAttribute("user");
        //TODO: check if user is null
        EntityManager em = DBManager.getManager();
        String q = request.getParameter("q");
        MeshResponse meshResponse = new MeshResponse(200);
        Query query;
        if(q == null || q.isEmpty()) {
             query = em
                     .createNativeQuery("select C.* from client C, orders O, document D where D.cid=C.id and D.type=0 and C.id=O.cid and O.uid = :uid order by C.firstname asc, C.lastname asc, C.patronymic asc", mesh.db.Client.class)
                     .setParameter("uid", user.getId());
        } else {
            String sql = "select C.* from client C, document D where D.cid=C.id and D.type=0 and ";
            sql += "(" + mesh.util.detectField(URLDecoder.decode(q, "UTF-8").toLowerCase()) + ")";
            query = em.createNativeQuery(sql + " order by C.firstname asc, C.lastname asc, C.patronymic asc", mesh.db.Client.class);
        }
        List<mesh.db.Client> clients = query.getResultList();
        meshResponse.setData(clients.toArray());
        out.write(new json(meshResponse).toString());
    }
}
