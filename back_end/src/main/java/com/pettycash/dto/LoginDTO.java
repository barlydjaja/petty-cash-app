package com.pettycash.dto;

import com.pettycash.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class LoginDTO {
    private String username;
    private String password;
    private Set<Role> role;
    private long userId;
}
