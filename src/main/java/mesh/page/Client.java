package mesh.page;

import mesh.DBManager;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;

@WebServlet(value = "/client", name = "Client")
public class Client extends HttpServlet {

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("me") != null) {
            try {
                Integer cid = Integer.parseInt(request.getParameter("id"));
                EntityManager em = DBManager.getManager();
                em.clear();
                mesh.db.Client client = (mesh.db.Client)em.createQuery("select C from Client C join fetch C.documents where C.id = :cid")
                        .setParameter("cid", cid)
                        .getSingleResult();
                request.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                request.setAttribute("client", client);
                request.getRequestDispatcher("WEB-INF/page/client.jsp").include(request, response);
            } catch(Exception ex) {
                ex.printStackTrace();
                response.sendRedirect("/");
            }
        } else {
            response.sendRedirect("/");
        }
    }
}
