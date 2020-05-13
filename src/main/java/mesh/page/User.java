package mesh.page;

import mesh.DBManager;
import mesh.db.Role;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/user", name = "User")
public class User extends HttpServlet {
    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mesh.db.User me;
        if((me = (mesh.db.User) request.getSession().getAttribute("me")) != null && me.getRole().getAccess().contains("admin")) {
            try {
                Integer uid = Integer.parseInt(request.getParameter("id"));
                EntityManager em = DBManager.getManager();
                mesh.db.User user = (mesh.db.User)em.createQuery("select U from User U where U.id = :uid")
                        .setParameter("uid", uid)
                        .getSingleResult();
                List<mesh.db.Role> roles = em.createQuery("select R from Role R order by R.id").getResultList();
                request.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                request.setAttribute("user", user);
                request.setAttribute("roles", roles);
                request.getRequestDispatcher("WEB-INF/page/user.jsp").include(request, response);
            } catch(Exception ex) {
                ex.printStackTrace();
                response.sendRedirect("/");
            }
        } else {
            response.sendRedirect("/");
        }
    }
}
