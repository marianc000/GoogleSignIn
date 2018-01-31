/*
 */
package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static server.OpenId.CODE;
import static server.OpenId.EMAIL;
import static server.OpenId.NONCE;
import static server.OpenId.STATE;
import static server.OpenId.TOKEN_ENDPOINT;

/**
 *
 * @author caikovsk
 */
@WebServlet(name = "MyAuthServlet", urlPatterns = {"/test2"})
public class MyAuthServlet extends HttpServlet {

    OpenId openId = new OpenId();

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter(CODE);
        String receivedState = request.getParameter(STATE); // state should also include last served url, so that the user can return to the page where he logs in
        String savedState = (String) request.getSession().getAttribute(STATE);
        String savedNonce = (String) request.getSession().getAttribute(NONCE);

        System.out.println(code);
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
                        System.out.println(email);
                        loggedInResponse(request, response);
                    } else {
                        loggedOutResponse(request, response, "Nonces are different: " + savedNonce + "!=" + receivedNonce);
                    }
                } else {
                    loggedOutResponse(request, response, "Id token is missing");
                }
            } else {
                loggedOutResponse(request, response, "States are different: " + savedState + "!=" + receivedState);
            }
        } else {
            loggedOutResponse(request, response, "Code is null");
        }

    }

    void loggedOutResponse(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String newState = openId.getState();
            request.getSession().setAttribute(STATE, newState);
            String newNonce = openId.getNonce();
            request.getSession().setAttribute(NONCE, newNonce);
            out.println("<a href='" + openId.getUrl(newState, newNonce) + "'>Click</a>");
            out.println("<p>" + msg + "</p>");
        }
    }

    void loggedInResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {

            //     out.println("code=" + code + "; email=" + email);
            out.println("OK");
        }
    }

    String readUserEmailFromGraphAPI(String token) throws IOException {
        try (JsonReader jsonReader = Json.createReader(getURLInputStream(token))) {
            JsonObject obj = jsonReader.readObject();
            System.out.println("<readGraphAPI: " + obj.toString());
            return obj.getString("email");
        }
    }

    Reader getURLInputStream(String token) throws IOException {
        return new InputStreamReader(getGraphAPIURL(token).openStream());
    }

    URL getGraphAPIURL(String token) throws MalformedURLException {

        URL url = new URL("https://graph.facebook.com/v2.11/me?access_token=" + token + "&debug=all&fields=email&format=json&method=get&pretty=0" /*+ getAppSecretProof(token)*/);
        System.out.println("url=" + url);
        return url;
    }
}
