package com.pettycash.config;

import com.pettycash.dto.LoginDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JWTGenerator {
    public String generate(LoginDTO loginDTO){
        Claims claims = Jwts.claims();

        claims.put("username", loginDTO.getUsername());
        claims.put("password", loginDTO.getPassword());
        claims.put("userId", loginDTO.getUserId());

        return Jwts.builder().setClaims(claims)
                .signWith(SignatureAlgorithm.HS512,"PETTYCASH")
                .claim("userId",loginDTO.getUserId())
                .compact();

    }
}
