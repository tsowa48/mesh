package mesh.page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(value = "/index", name = "Index")
public class Index extends HttpServlet {

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mesh.util.rb = ResourceBundle.getBundle("mesh", request.getLocale());
        if(request.getSession().getAttribute("me") == null) {
            response.sendRedirect("/login");
        } else {
            mesh.db.User user = (mesh.db.User)request.getSession().getAttribute("me");
            request.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            request.getRequestDispatcher("WEB-INF/page/" + user.getRole().getAccess() + ".jsp").include(request, response);
        }
    }
}
