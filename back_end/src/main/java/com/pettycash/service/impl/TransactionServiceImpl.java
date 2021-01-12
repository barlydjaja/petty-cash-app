package com.pettycash.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pettycash.dto.LandingPageDTO;
import com.pettycash.dto.TransactionDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;
import com.pettycash.repository.TransactionRepository;
import com.pettycash.repository.UserRepository;
import com.pettycash.service.TransactionService;
import com.pettycash.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService{
	
	private static final String INCOME = "income";
	private static final String OUTCOME = "outcome";

	@Autowired
	private TransactionRepository repo;
	
	@Autowired
	private UserService userService;

	public Transaction addTransaction(TransactionDTO dto, boolean receipt, User user, TransactionType transactionType) {
		
		Transaction newTransaction = new Transaction();
		if(receipt) {
			newTransaction.setReceipt(INCOME);
			newTransaction.setAmount(dto.getAmount());
			newTransaction.setDescription(dto.getDescription());
			newTransaction.setUser(user);
			newTransaction.setTransactionDate(new Date());
			newTransaction.setTransactionType(transactionType);
			
			newTransaction.setResidue(user.getAccountBalance() + newTransaction.getAmount());
			userService.updateUserBalance(user.getUserId(), newTransaction.getAmount());
			
		} else {
			newTransaction.setReceipt(OUTCOME);
			newTransaction.setAmount(toNegative(dto.getAmount()));
			newTransaction.setDescription(dto.getDescription());
			newTransaction.setUser(user);
			newTransaction.setTransactionDate(new Date());
			newTransaction.setTransactionType(transactionType);
			
			newTransaction.setResidue(user.getAccountBalance() + newTransaction.getAmount());
			userService.updateUserBalance(user.getUserId(), newTransaction.getAmount());
			
		}
		
		
		return repo.save(newTransaction);
	}
	
	public List<Transaction> getAllByUser(User user) {
		return repo.findAllByUserOrderByTransactionDateAsc(user);
	}

	public LandingPageDTO getView(User user, List<Transaction> transaction) {
		LandingPageDTO result = new LandingPageDTO();
		result.setCode(user.getCode());
		result.setDepartment(user.getDepartment());
		result.setStartBalance(user.getStartBalance());
		result.setTransaction(transaction);
		result.setName(user.getName());
		
		long temp = user.getStartBalance();
		
		for(int i = 0; i < transaction.size(); i++) {
			result.getTransaction().get(i).setResidue(temp + result.getTransaction().get(i).getAmount());
			temp = result.getTransaction().get(i).getResidue();
		}
		
		result.setTotal(temp);
		
		return result;
	}
	
	private long toNegative(long amount) {
		return 0 - amount;
	}

	public Page<Transaction> getAllWithPaging(User user, Pageable pageable) {
		return repo.findByUserOrderByTransactionDateAsc(user, pageable);
	}

}
