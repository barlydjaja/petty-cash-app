package com.pettycash.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String department;
    private String name;
    private String code;
    private long balance;
}
