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
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(value = "/api/loan", name = "Loan api")
public class Loan extends HttpServlet {

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        mesh.db.User me = (mesh.db.User)request.getSession().getAttribute("user");
        //TODO: check if user is null
        EntityManager em = DBManager.getManager();
        String lid = request.getParameter("id");
        MeshResponse meshResponse = new MeshResponse(200);
        Query query;
        if(lid == null || lid.isEmpty()) {
            query = em.createQuery("select L from Loan L order by L.name asc");
        } else {
            query = em
                    .createQuery("select L from Loan L where L.id = :lid order by L.name asc")
                    .setParameter("lid", Integer.parseInt(lid));
        }
        List<mesh.db.Loan> loans = query.getResultList();
        meshResponse.setData(loans.toArray());
        out.write(new json(meshResponse).toString());
    }

    @POST
    @Transactional
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        mesh.db.User me = (mesh.db.User) request.getSession().getAttribute("user");
        //TODO: check if user is null
        EntityManager em = DBManager.getManager();
        json jData = new json(request.getReader().lines().collect(Collectors.joining(" ")));
        MeshResponse meshResponse = new MeshResponse(200);
        String name = jData.get("name");
        Double min_amount = Double.valueOf(jData.get("min_amount"));
        Double max_amount = Double.valueOf(jData.get("max_amount"));
        Integer min_term = Integer.valueOf(jData.get("min_term"));
        Integer max_term = Integer.valueOf(jData.get("max_term"));
        Double min_percent = Double.valueOf(jData.get("min_percent"));
        Double max_percent = Double.valueOf(jData.get("max_percent"));
        Double min_solvency = Double.valueOf(jData.get("min_solvency"));
        Double max_solvency = Double.valueOf(jData.get("max_solvency"));
        mesh.db.Loan loan = (mesh.db.Loan)em
                .createNativeQuery("insert into loan(name, min_amount, max_amount, min_term, max_term, min_percent, max_percent, min_solvency, max_solvency) values(:name, :min_amount, :max_amount, :min_term, :max_term, :min_percent, :max_percent, :min_solvency, :max_solvency) returning *", mesh.db.Loan.class)
                .setParameter("name", name)
                .setParameter("min_amount", min_amount)
                .setParameter("max_amount", max_amount)
                .setParameter("min_term", min_term)
                .setParameter("max_term", max_term)
                .setParameter("min_percent", min_percent)
                .setParameter("max_percent", max_percent)
                .setParameter("min_solvency", min_solvency)
                .setParameter("max_solvency", max_solvency)
                .getSingleResult();
        meshResponse.setData(loan);
        out.write(new json(meshResponse).toString());
    }
}
