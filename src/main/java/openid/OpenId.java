package openid;

import static folder.Constants.CLIENT_ID;
import static folder.Constants.CLIENT_SECRET;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class OpenId {

    public static String CODE = "code", STATE = "state",EMAIL="email", NONCE = "nonce", TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token", REDIRECT_URI = "http://localhost:8080/test/test2",
            ID_TOKEN = "id_token";
    String BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth"; // HTTP connections are refused
    Random r = new Random();

    public static void main(String... args) {
        System.out.println(new OpenId().getUrl("TEST_STATE", "TEST_NONCE"));
    }

    /*
            gapi.auth2.init({
            client_id: clientId,
            fetch_basic_profile: false, // By default, the fetch_basic_profile parameter of gapi.auth2.init() is set to true, which will automatically add 'email profile openid' as scope.
            scope: 'email',
            ux_mode: 'redirect', // The UX mode to use for the sign-in flow. By default, it will open the consent flow in a popup. Valid values are popup and redirect.
            redirect_uri: 'http://localhost:8080/test/' //If using ux_mode='redirect', this parameter allows you to override the default redirect_uri that will be used at the end of the consent flow. 
                    //// The default redirect_uri is the current URL stripped of query parameters and hash fragment.
        })
     */
    String getUrl(String state, String nonce) {
        return BASE_URL + "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scope=email&response_type=code&" + NONCE + "=" + nonce + "&" + STATE + "=" + state;

    }

    String getParamsForTokenEndpoint(String code) {
        return "code=" + code + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + REDIRECT_URI + "&grant_type=authorization_code";
    }

    public String getState() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    public String getNonce() {
        return String.valueOf(r.nextInt(Integer.MAX_VALUE));
    }

    String exchangeCodeForToken(String code) throws IOException {
        try (JsonReader jsonReader = Json.createReader(http.post(TOKEN_ENDPOINT, getParamsForTokenEndpoint(code)))) {
            JsonObject obj = jsonReader.readObject();
            System.out.println("<exchangeCodeForToken: " + obj.toString());
            return obj.getString(ID_TOKEN);
        }
    }

    public JsonObject decodeIdToken(String idToken) {

        String secondStr = idToken.split("\\.")[1];
        byte[] payloadBytes = Base64.getDecoder().decode(secondStr);
        String json = new String(payloadBytes);
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject jobj = jsonReader.readObject();
        System.out.println(jobj);
        return jobj;
    }
    HttpUtils http = new HttpUtils();
}
