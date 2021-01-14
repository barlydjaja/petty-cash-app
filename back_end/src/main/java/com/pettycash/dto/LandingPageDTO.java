package com.pettycash.dto;

import java.util.List;

import com.pettycash.entity.Transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LandingPageDTO {

    private String name;
    private long startBalance;
    private String code;
    private String department;
    List<Transaction> transaction;
    private long total;

}
