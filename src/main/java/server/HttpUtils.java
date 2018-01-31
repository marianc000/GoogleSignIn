/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author mcaikovs
 */
public class HttpUtils {

    public InputStream post(String url, String params) throws IOException {
        System.out.println("post: url="+url);
        System.out.println("post: "+params);
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        try (OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream())) {
            out.write(params);
        }

        return con.getInputStream();
    }
}
