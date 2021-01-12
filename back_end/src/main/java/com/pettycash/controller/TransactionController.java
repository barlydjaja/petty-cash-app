package com.pettycash.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pettycash.dto.TransactionDTO;
import com.pettycash.dto.TransactionTypeDTO;
import com.pettycash.dto.UserDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;
import com.pettycash.service.TransactionService;
import com.pettycash.service.TransactionTypeService;
import com.pettycash.service.UserService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/add")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	TransactionTypeService transactionTypeService;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/incomeTransaction")
	@CrossOrigin
	public ResponseEntity<Transaction> addIncomeTransaction(@RequestBody TransactionDTO transactionDTO) throws NotFoundException{

		User user = userService.getUserById(transactionDTO.getUserId());
		TransactionType transactionType = transactionTypeService.getTypeById(transactionDTO.getTransactionTypeId());
		Transaction transaction = transactionService.addTransaction(transactionDTO, true, user, transactionType);
		
		return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
	}
	
	@PostMapping("/outcomeTransaction")
	@CrossOrigin
	public ResponseEntity<Transaction> addOutcomeTransaction(@RequestBody TransactionDTO transactionDTO) throws NotFoundException{

		User user = userService.getUserById(transactionDTO.getUserId());
		TransactionType transactionType = transactionTypeService.getTypeById(transactionDTO.getTransactionTypeId());
		Transaction transaction = transactionService.addTransaction(transactionDTO, false, user, transactionType);
		
		return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
	}
	
	@PostMapping("/user")
	@CrossOrigin
	public ResponseEntity<User> addNewUser(@RequestBody UserDTO userDTO){
		User user = userService.addUser(userDTO);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping("/transactionType")
	@CrossOrigin
	public ResponseEntity<TransactionType> addNewTransactionType(@RequestBody TransactionTypeDTO name){
		TransactionType type = transactionTypeService.addType(name.getName());
		
		return new ResponseEntity<TransactionType>(type, HttpStatus.OK);
	}
}
