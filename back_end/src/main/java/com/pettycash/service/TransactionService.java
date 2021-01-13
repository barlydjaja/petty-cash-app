package com.pettycash.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pettycash.dto.LandingPageDTO;
import com.pettycash.dto.TransactionDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;

import javassist.NotFoundException;

public interface TransactionService{
	Transaction addTransaction(TransactionDTO dto, User user, TransactionType transactionType); //receipt true if income and false if outcome
	List<Transaction> getAllByUser(User user);
	LandingPageDTO getView(User user, List<Transaction> transaction);
	Page<Transaction> getAllWithPaging(User user, Pageable pageable);
	boolean deleteTransaction(long transactionId);
	boolean updateTransaction(long transactionId, TransactionDTO dto, long userId)  throws NotFoundException;
}