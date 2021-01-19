package com.pettycash.config;

import com.pettycash.dto.LoginDTO;
import com.pettycash.entity.Role;
import com.pettycash.service.RoleService;
import com.pettycash.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("unchecked")
public class JWTValidator {
    private static final String secret = "PETTYCASH";

    public LoginDTO validate(String token) {

        LoginDTO userDomain = null;
        try {
            Claims body= Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();

            userDomain = new LoginDTO();

            int a = (int) body.get("userId");
            userDomain.setUsername((String)body.get("username"));
            userDomain.setPassword((String)body.get("password"));
            userDomain.setUserId((long) a);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        return userDomain;
    }
}
