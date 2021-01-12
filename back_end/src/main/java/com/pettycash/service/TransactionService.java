package com.pettycash.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pettycash.dto.LandingPageDTO;
import com.pettycash.dto.TransactionDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;

public interface TransactionService{
	Transaction addTransaction(TransactionDTO dto, boolean receipt, User user, TransactionType transactionType); //receipt true if income and false if outcome
	List<Transaction> getAllByUser(User user);
	LandingPageDTO getView(User user, List<Transaction> transaction);
	Page<Transaction> getAllWithPaging(User user, Pageable pageable);
}