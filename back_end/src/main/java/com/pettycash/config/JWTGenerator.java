package com.pettycash.config;

import com.pettycash.dto.LoginDTO;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class JWTGenerator {
    public String generate(LoginDTO loginDTO){
        Claims claims = Jwts.claims();

        claims.put("username", loginDTO.getUsername());
        claims.put("password", loginDTO.getPassword());
        claims.put("userId", loginDTO.getUserId());
        claims.put("roles", loginDTO.getRole());

        return Jwts.builder().setClaims(claims)
                .signWith(SignatureAlgorithm.HS512,"PETTYCASH")
                .claim("userId",loginDTO.getUserId())
                .compact();

    }
}
