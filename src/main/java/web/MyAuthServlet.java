/*
 */
package web;

import static folder.UserResource.checkAndGetEmail;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author caikovsk
 */
@WebServlet(name = "MyAuthServlet", urlPatterns = {"/test2"})
public class MyAuthServlet extends HttpServlet {

    static String CODE = "code";

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter(CODE);
        String email = "";
        System.out.println(code);
        if (code != null) {
            try {
                email = checkAndGetEmail(code);
            } catch ( Exception ex) {
                ex.printStackTrace();
            }
        }
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            if (code == null) {
                out.println("<a href='https://accounts.google.com/o/oauth2/v2/auth?client_id=517342657945-qv1ltq618ijj9edusgnnbpmbghkatc2q.apps.googleusercontent.com&redirect_uri=http://localhost:8080/test/test2&scope=email&response_type=code'>Click</a>");
            } else {
                out.println("code=" + code + "; email=" + email);
            }

        }
    }
}
