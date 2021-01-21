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
	Page<Transaction> findByOrderByTransactionDateAsc(Pageable pageable);
	List<Transaction> findAllByOrderByTransactionDateAsc();
	Page<Transaction> findAllByPendingDeleteOrderByTransactionDateAsc(String pendingDelete,Pageable pageable);
	Page<Transaction> findAllByPendingUpdateOrderByTransactionDateAsc(String pendingUpdate, Pageable pageable);
}
