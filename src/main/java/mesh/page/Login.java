package mesh.page;

import mesh.DBManager;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/login", name = "Login")
public class Login extends HttpServlet {

    @POST
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        EntityManager em = DBManager.getManager();
        List<mesh.db.User> user = em
                .createNativeQuery("select U.* from users U where U.login = :login and U.password = :password", mesh.db.User.class)
                .setParameter("login", login)
                .setParameter("password", password)
                .getResultList();
        if(user.size() == 1) {
            request.getSession(true).setAttribute("me", user.get(0));
            response.sendRedirect("/");
        } else {
            request.setAttribute("error", "has-error");
            request.getRequestDispatcher("WEB-INF/page/login.jsp").include(request, response);
        }
    }

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("me") == null) {
            request.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            request.getRequestDispatcher("WEB-INF/page/login.jsp").include(request, response);
        } else {
            response.sendRedirect("/");
        }
    }
}
