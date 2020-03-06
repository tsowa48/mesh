package mesh.api;

import json.json;
import mesh.HibernateUtil;
import mesh.MeshResponse;
import mesh.util;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet("/remove")
public class Remove extends HttpServlet {

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                String sql = "delete from :table where id = :id";
                String tableName= "";
                Integer id = 0;
                //TODO: cascade remove
                if(data.contains("uid")) {//User
                    id = jData.<Integer>get("uid");
                    tableName = "users";
                } else if(data.contains("cid")) {//Client ???
                    id = jData.<Integer>get("cid");
                    //tableName = "client";
                } else if(data.contains("lid")) {//Loan
                    id = jData.<Integer>get("lid");
                    tableName = "loan";
                } else if(data.contains("did")) {//Document ???
                    id = jData.<Integer>get("did");
                    tableName = "document";
                } else if(data.contains("oid")) {//Order
                    id = jData.<Integer>get("oid");
                    tableName = "orders";
                }
                Query query = session.createNativeQuery(sql);
                query.setParameter("table", tableName);
                query.setParameter("id", id);
                query.executeUpdate();
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
