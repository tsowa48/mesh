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

@WebServlet(value = "/loan", name = "Loan")
public class Loan extends HttpServlet {
    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mesh.db.User me;
        if((me = (mesh.db.User) request.getSession().getAttribute("me")) != null && me.getRole().getAccess().contains("admin")) {
            try {
                Integer lid = Integer.parseInt(request.getParameter("id"));
                EntityManager em = DBManager.getManager();
                mesh.db.Loan loan = (mesh.db.Loan)em.createQuery("select L from Loan L where L.id = :lid")
                        .setParameter("lid", lid)
                        .getSingleResult();
                request.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                request.setAttribute("loan", loan);
                request.getRequestDispatcher("WEB-INF/page/loan.jsp").include(request, response);
            } catch(Exception ex) {
                ex.printStackTrace();
                response.sendRedirect("/");
            }
        } else {
            response.sendRedirect("/");
        }
    }
}
