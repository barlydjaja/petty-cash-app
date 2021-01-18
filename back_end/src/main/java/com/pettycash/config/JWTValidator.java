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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

            userDomain.setUsername((String)body.get("username"));
            userDomain.setPassword((String)body.get("password"));
            List<Role> roles = (List<Role>) body.get("roles");

            Set<Role> roleSet = new HashSet<>(roles);

            userDomain.setRole(roleSet);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        return userDomain;
    }
}
