package com.pettycash.dto;

import com.pettycash.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDTO {
    private long userId;
    private String token;
    private Role role;
}
