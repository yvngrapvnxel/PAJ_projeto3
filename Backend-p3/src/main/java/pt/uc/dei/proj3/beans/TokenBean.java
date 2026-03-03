package pt.uc.dei.proj3.beans;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenBean implements Serializable {


    private String token; // = UUID.randomUUID().toString();


    public static String generateToken (){
        SecureRandom sr = new SecureRandom();
        byte[] token = new byte[16];
        sr.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
