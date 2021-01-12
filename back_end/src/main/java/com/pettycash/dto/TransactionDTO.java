package com.pettycash.dto;

import java.util.Date;

import javax.persistence.Column;

import com.pettycash.entity.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionDTO {

	private String description;
	private String receipt;
	private long amount;
	private long transactionTypeId;
	private long userId;
	
}
