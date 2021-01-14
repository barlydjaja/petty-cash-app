package com.pettycash.controller;

import com.pettycash.dto.LoginDTO;
import com.pettycash.dto.TokenDTO;
import com.pettycash.entity.User;
import com.pettycash.service.LoginService;
import com.pettycash.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secured")
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;

    @Autowired
    public LoginController(LoginService loginService, UserService userService){
        this.loginService = loginService;
        this.userService = userService;
    }

    @CrossOrigin
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO userDomain){

        TokenDTO loginDomain = new TokenDTO();
        HttpStatus status ;
        try {
            loginDomain =loginService.login(userDomain);
            status = HttpStatus.OK;
        }
        catch (Exception e){
            System.out.println(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(loginDomain,status);
    }
}
