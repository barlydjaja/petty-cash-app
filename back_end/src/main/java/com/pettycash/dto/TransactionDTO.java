package com.pettycash.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TransactionDTO {

    private String description;
    private String receipt;
    private long amount;
    private long transactionTypeId;
    private long userId;
    private Date date;

}
