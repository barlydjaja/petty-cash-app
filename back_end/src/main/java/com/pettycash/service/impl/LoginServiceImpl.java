package com.pettycash.service.impl;

import com.pettycash.config.JWTGenerator;
import com.pettycash.dto.JWTAuthenticationToken;
import com.pettycash.dto.LoginDTO;
import com.pettycash.dto.TokenDTO;
import com.pettycash.entity.User;
import com.pettycash.service.LoginService;
import com.pettycash.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final UserService userService;

    @Autowired
    public LoginServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) throws NotFoundException {
        User userTable = userService.getUserByUsername(loginDTO.getUsername());

        if (!passwordEncoder.matches(loginDTO.getPassword(), userTable.getPassword())){
            throw new NotFoundException("wrong password");
        }

        else{
            loginDTO.setUserId(userTable.getUserId());
            loginDTO.setUsername(userTable.getUsername());
            loginDTO.setRole(userTable.getRoles());
            JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(jwtGenerator.generate(loginDTO));

            String payload = jwtAuthenticationToken.getToken();
            TokenDTO loginDomain = new TokenDTO();
            loginDomain.setToken(payload);
            loginDomain.setUserId(userTable.getUserId());
            loginDomain.setRole(userTable.getRoles());
            return loginDomain;
        }
    }
}
