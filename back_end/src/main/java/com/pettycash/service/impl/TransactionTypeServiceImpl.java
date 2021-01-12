package com.pettycash.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pettycash.entity.TransactionType;
import com.pettycash.repository.TransactionTypeRepository;
import com.pettycash.service.TransactionTypeService;

import javassist.NotFoundException;

@Service
public class TransactionTypeServiceImpl implements TransactionTypeService{

	@Autowired
	private TransactionTypeRepository repo;
	
	public TransactionType addType(String type) {
		TransactionType newType = new TransactionType();
		newType.setTransactionTypeName(type);
		newType.setCreatedDate(new Date());
		newType.setUpdatedDate(new Date());
		
		return repo.save(newType);
	}

	public TransactionType getTypeById(long id) throws NotFoundException {
		if(repo.getOne(id) == null) {
			throw new NotFoundException("transaction type not found ");
		}
		
		return repo.getOne(id);
	}

	public List<TransactionType> getAllTransactionType() {
		return repo.findAll();
	}

}
