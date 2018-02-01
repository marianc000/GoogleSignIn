/*
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static server.OpenId.CODE;
import static server.OpenId.EMAIL;
import static server.OpenId.NONCE;
import static server.OpenId.STATE;

/**
 *
 * @author caikovsk
 */
@WebServlet(name = "MyAuthServlet", urlPatterns = {"/server"})
public class MyAuthServlet extends HttpServlet {

    OpenId openId = new OpenId();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String code = request.getParameter(CODE);
        HttpSession session = request.getSession();
        String receivedState = request.getParameter(STATE); // state should also include last served url, so that the user can return to the page where he logs in
        String savedState = (String) session.getAttribute(STATE);
        String newState = openId.getState();
        session.setAttribute(STATE, newState);
        String savedNonce = (String) session.getAttribute(NONCE);
        String newNonce = openId.getNonce();
        session.setAttribute(NONCE, newNonce);

        System.out.println(code);
        try (PrintWriter out = response.getWriter()) {
            if (code != null) {
                //On the server, you must confirm that the state received from Google matches the session token you created in Step 1. This round-trip verification helps to ensure that the user, not a malicious script, is making the request.
                if (savedState.equals(receivedState)) {
                    String idToken = openId.exchangeCodeForToken(code);
                    if (idToken != null) {
                        System.out.println(idToken);
                        JsonObject json = openId.decodeIdToken(idToken);
                        System.out.println(json);
                        String receivedNonce = json.getString(NONCE);
                        if (savedNonce.equals(receivedNonce)) {
                            String email = json.getString(EMAIL);
                            System.out.println("email="+email);
                            out.println("<p>Hello " + email + "</p>");
                            return;
                        } else {
                            out.println("Nonces differ");
                        }
                    } else {
                        out.println("Id token is missing");
                    }
                } else {
                    out.println("States are different");
                }
            } else {
                out.println("<p>Code is null</p>");
            }
            out.println("<a href='" + openId.getUrl(newState, newNonce) + "'>Click to sign in</a>");
        }
    }
}
