/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package folder;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import static folder.Constants.CLIENT_ID;
import static folder.Constants.JSON_FACTORY;
import static folder.Constants.TRANSPORT;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author mcaikovs
 */
@Path("/test")
public class UserResource {

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public User getToken(String token) throws GeneralSecurityException, IOException {
        System.out.println(">getToken");
        return new User(checkAndGetEmail(token));
    }

  public static  String checkAndGetEmail(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, JSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

// (Receive idTokenString by HTTPS POST)
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            System.out.println("email=" + email + "; emailVerified=" + emailVerified + "; name=" + name + "; pictureUrl=" + pictureUrl + "; familyName=" + familyName);
            return email;
        } else {
            System.out.println("Invalid ID token.");
            throw new WebApplicationException("Invalid token", Response.Status.FORBIDDEN);
        }
    }
}
