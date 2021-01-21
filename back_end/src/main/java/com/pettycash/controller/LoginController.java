package com.pettycash.controller;

import com.pettycash.dto.LoginDTO;
import com.pettycash.dto.TokenDTO;
import com.pettycash.entity.Role;
import com.pettycash.entity.User;
import com.pettycash.repository.UserRepository;
import com.pettycash.service.LoginService;
import com.pettycash.service.RoleService;
import com.pettycash.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/secured")
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;

    @Autowired
    public LoginController(LoginService loginService, UserService userService, RoleService roleService, UserRepository userRepository){
        this.loginService = loginService;
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO userDomain){

        System.out.println("login");

        TokenDTO loginDomain = new TokenDTO();
        HttpStatus status ;
        try {
            loginDomain =loginService.login(userDomain);
            status = HttpStatus.OK;
        }
        catch (Exception e){
            System.out.println(e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(loginDomain,status);
    }

    @PostMapping("/assign")
    public ResponseEntity<User> assign(@RequestBody Map<String, Long> request) throws NotFoundException {
        User user = userService.getUserById(request.get("userId"));
        Role role = roleService.getById(request.get("roleId"));

        user.setRole(role);
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
