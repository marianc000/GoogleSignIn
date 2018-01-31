package folder;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

public class Constants {

    public static final String CLIENT_ID = "517342657945-qv1ltq618ijj9edusgnnbpmbghkatc2q.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "0PVwYgPuLpH3PFHljPkbtJeP";
    /*
   * Default HTTP transport to use to make HTTP requests.
     */
    public static final HttpTransport TRANSPORT = new NetHttpTransport();

    /*
   * Default JSON factory to use to deserialize JSON.
     */
    public static final JacksonFactory JSON_FACTORY = new JacksonFactory();
}
