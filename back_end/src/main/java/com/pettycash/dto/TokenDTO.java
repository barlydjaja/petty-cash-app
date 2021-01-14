package com.pettycash.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDTO {
    private long userId;
    private String token;
    private String role;
}
