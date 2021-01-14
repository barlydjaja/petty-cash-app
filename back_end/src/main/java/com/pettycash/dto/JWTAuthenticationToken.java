package com.pettycash.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
public class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String token;
    public JWTAuthenticationToken(String token) {
        super(null, null);
        this.token=token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
