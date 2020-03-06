package mesh.api;

import json.json;
import mesh.MeshResponse;
import mesh.db.User;
import mesh.util;

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
@WebServlet("/auth")
public class Auth extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json;charset=UTF-8");
    String authString = request.getHeader("Authorization");
    PrintWriter out = response.getWriter();
    User user = util.tryLogin(authString);
    if(user == null) {
      response.setHeader("WWW-Authenticate", "Basic");
      out.write(new json(new MeshResponse(401)).toString());
      response.setStatus(401);
    } else {
      //user.password = null;
      user.orders = null;
      request.getSession(true).setAttribute("user", user);
      out.write(new json(new MeshResponse(200, user)).toString());
    }
  }
}
