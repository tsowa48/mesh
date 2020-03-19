package mesh.page;

import mesh.db.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(value = "/index", name = "Index")
public class Index extends HttpServlet {

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mesh.util.rb = ResourceBundle.getBundle("mesh", request.getLocale());
        if(request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/login");
        } else {
            User user = (User)request.getSession().getAttribute("user");
            request.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            request.getRequestDispatcher("WEB-INF/page/" + user.getRole().getAccess() + ".jsp").include(request, response);
        }
    }
}
