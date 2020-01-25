package mesh.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.json;
import mesh.MeshResponse;
import mesh.util;

/**
 *
 * @author tsowa
 */
public class Add extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("application/json;charset=utf-8");
    PrintWriter out = response.getWriter();
    try {
      if(util.isAuthorized(request)) {
        BufferedReader br = request.getReader();
        String data = br.lines().collect(Collectors.joining(""));
System.out.println("\n\ndata='" + data + "'\n\n");//DEBUG
        //TODO: add user, client
      } else {
        response.setHeader("WWW-Authenticate", "Basic realm=\"api.mesh\"");
        out.write(new json(new MeshResponse(401)).toString());
        response.setStatus(401);
      }
    } catch(Exception ex) {
      ex.printStackTrace();//DEBUG
      out.write(new json(new MeshResponse(499)).toString());
    }
  }

}
