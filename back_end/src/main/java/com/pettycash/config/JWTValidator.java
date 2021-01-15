package com.pettycash.config;

import com.pettycash.dto.LoginDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JWTValidator {
    private static final String secret = "PETTYCASH";

    public LoginDTO validate(String token) {

        LoginDTO userDomain = null;
        try {

            Claims body= Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();

            userDomain = new LoginDTO();

            userDomain.setUsername(body.getSubject());
            userDomain.setPassword((String)body.get("password"));
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        return userDomain;
    }
}
