package com.pettycash.service;

import java.util.List;

import com.pettycash.entity.TransactionType;

import javassist.NotFoundException;

public interface TransactionTypeService {
	TransactionType addType(String type);
	TransactionType getTypeById(long id) throws NotFoundException;
	List<TransactionType> getAllTransactionType();
}
