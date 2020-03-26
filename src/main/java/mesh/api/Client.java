package mesh.api;

import json.*;
import mesh.DBManager;
import mesh.MeshResponse;
import mesh.db.Document;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(value = "/api/client", name = "Client api")
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

    @POST
    @Transactional
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        mesh.db.User user = (mesh.db.User) request.getSession().getAttribute("user");
        //TODO: check if user is null
        EntityManager em = DBManager.getManager();
        json jData = new json(request.getReader().lines().collect(Collectors.joining(" ")));
        MeshResponse meshResponse = new MeshResponse(200);
        String firstName = jData.get("firstName");
        String lastName = jData.get("lastName");
        String patronymic = jData.get("patronymic");
        String birth = jData.get("birth");
        String sex = jData.get("sex");
        String address = jData.get("address");
        //TODO: fill documents
        String doc0s = jData.get("doc_s_0");
        String doc0n = jData.get("doc_n_0");
        String doc0i = jData.get("doc_i_0");
        mesh.db.Client client = (mesh.db.Client)em
                .createNativeQuery("insert into client(firstname, lastname, patronymic, birth, ismale, address) values(:fn, :ln, :p, :b, :m, :a) returning *", mesh.db.Client.class)
                .setParameter("fn", firstName)
                .setParameter("ln", lastName)
                .setParameter("p", patronymic)
                .setParameter("b", birth)
                .setParameter("m", sex.equals("1"))
                .setParameter("a", address)
                .getSingleResult();
        mesh.db.Document pasport = (mesh.db.Document)em
                .createNativeQuery("insert into document(serial, number, issued, cid, type) values(:s, :n, :i, :c, :t) returning *", mesh.db.Document.class)
                .setParameter("s", doc0s)
                .setParameter("n", doc0n)
                .setParameter("i", doc0i)
                .setParameter("c", client.getId())
                .setParameter("t", 0)
                .getSingleResult();
        client.addDocument(pasport);
        meshResponse.setData(client);
        out.write(new json(meshResponse).toString());
    }
}
