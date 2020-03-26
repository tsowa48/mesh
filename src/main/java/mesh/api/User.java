package mesh.api;

import json.json;
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
import java.util.List;

@WebServlet(value = "/api/user", name = "User api")
public class User extends HttpServlet {

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        mesh.db.User user = (mesh.db.User)request.getSession().getAttribute("user");
        //TODO: check if user is null
        EntityManager em = DBManager.getManager();
        String uid = request.getParameter("id");
        MeshResponse meshResponse = new MeshResponse(200);
        Query query;
        if(uid == null || uid.isEmpty()) {
            query = em.createNativeQuery("select U.* from users U, role R where U.rid=R.id order by U.fio asc", mesh.db.User.class);
        } else {
            query = em
                    .createNativeQuery("select U.* from users U, role R where U.rid=R.id ad U.id = :uid order by U.fio asc", mesh.db.User.class)
                    .setParameter("uid", Integer.parseInt(uid));
        }
        List<mesh.db.User> users = query.getResultList();
        meshResponse.setData(users.toArray());
        out.write(new json(meshResponse).toString());
    }
}
