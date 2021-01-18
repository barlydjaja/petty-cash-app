package com.pettycash.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pettycash.entity.Transaction;
import com.pettycash.entity.User;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	List<Transaction> findAllByUserOrderByTransactionDateAsc(User user);
	Page<Transaction> findByUserOrderByTransactionDateAsc(User user, Pageable pageable);
	List<Transaction> findAllByUserAndIsApprovedOrderByTransactionDateAsc(User user, String isApproved);
}
