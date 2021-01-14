package com.pettycash.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralResponse {

    private String response;
    private String message;
    private String path;
    private Date date;

}
