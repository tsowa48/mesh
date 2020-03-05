package mesh;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author tsowa
 */
@WebServlet("/index")
public class index extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.print("<!DOCTYPE html><html><head><title>api.mesh</title><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body>");
    ArrayList<String> apiList = new ArrayList<>();

    apiList.add("/auth");
    apiList.add("/search?q=Иванов");
    apiList.add("/debug");
    
    apiList.forEach(it -> {
      out.print("<a href='/mesh" + it + "'>" + it + "</a><br>");
    });
    out.print("</body></html>");
  }
}
