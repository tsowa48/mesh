package mesh.api;

import mesh.plugin.fssp.Region;
import mesh.plugin.fssp.fssp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author tsowa
 */
@WebServlet("/debug")
public class Debug extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
      Double ret = fssp.get("Коротких", "Владимир", "Николаевич", "20.04.1962", Region.LIPETSK);
      out.println(ret);
    }    
  }
}
