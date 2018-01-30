package web;

import static folder.Constants.CLIENT_ID;
import static folder.Constants.WEB_APP_CLIENT_ID;

public class Test {

    String BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";

    public static void main(String... args) {
        System.out.println(new Test().getUrl());

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
    String getUrl() {
        return BASE_URL + "?client_id=" + CLIENT_ID + "&redirect_uri=http://localhost:8080/test/&scope=email&response_type=code";
    }
}
