package mesh.api;

import mesh.DBManager;
import mesh.MeshResponse;
import json.json;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(value = "/api/role", name = "Role api")
public class Role extends HttpServlet {

    @GET
    @Transactional
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        mesh.db.User me = (mesh.db.User)request.getSession().getAttribute("me");
        MeshResponse meshResponse = new MeshResponse(200);
        if(me != null) {
            EntityManager em = DBManager.getManager();
            em.clear();
            String rid = request.getParameter("id");
            Query query;
            if (rid == null || rid.isEmpty()) {
                query = em.createNativeQuery("select R.* from role R order by R.access asc", mesh.db.Role.class);
            } else {
                query = em
                        .createNativeQuery("select R.* from role R where R.id = :rid order by R.access asc", mesh.db.Role.class)
                        .setParameter("rid", Integer.parseInt(rid));
            }
            List<mesh.db.Role> roles = query.getResultList();
            meshResponse.setData(roles.toArray());
        }
        out.write(new json(meshResponse).toString());
    }
}
