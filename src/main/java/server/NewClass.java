/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 *
 * @author mcaikovs
 */
public class NewClass {

    static String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI2YzAxOGIyMzNmZTJlZWY0N2ZlZGJiZGQ5Mzk4MTcwZmM5YjI5ZDgifQ.eyJhenAiOiI1MTczNDI2NTc5NDUtcXYxbHRxNjE4aWpqOWVkdXNnbm5icG1iZ2hrYXRjMnEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1MTczNDI2NTc5NDUtcXYxbHRxNjE4aWpqOWVkdXNnbm5icG1iZ2hrYXRjMnEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTY4NjczMDQ5NzEyODQ3OTg1NjYiLCJlbWFpbCI6Im1hcmlhbi5jYWlrb3Zza2lAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJCSVBBeDdKVEd3NmlONnhCV0dxQXZRIiwiZXhwIjoxNTE3NDA1MDYxLCJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJpYXQiOjE1MTc0MDE0NjF9.BFMMaK0axpw5Kc4UqS3bfUKxvlle2UuKAJtypKXe4XbLWpPgwEBnMRv8oNM-FLiY4sYTLZN13bx1vRYkyiWPgaLuAfwSVaqXdLtGDlbCmP7FDYLB2jlt2auwwGoem0WlttFdwZK30z9sjuAwkIU7-1j611KXuBHMoM86k2CHJ5u42yQ48dGH_w91gYc6grCUlv58Knz29XgxnWoWWx_5QXuG771jyD1afh9tAo_E5YIZgdZSQhJZAcoFcNTgVygFhc5CkZNbH6FJ7gOmTunap6nq7SO6FW8uUZz34I5n7HGLLGvYQaPGq-cMqCB0P1nNje3LY-IOGXOyNmZsCOgWsA";

    public static void main(String... args) throws IOException {
      //  System.out.println(Base64.getUrlDecoder().decode(idToken));
      parse(idToken);
    }

    public static void parse(String tokenString) throws IOException {
 
        String secondStr = tokenString.split("\\.")[1];
        byte[] payloadBytes = Base64.getDecoder().decode(secondStr);
        System.out.println(new String(payloadBytes));
         
    }
}
