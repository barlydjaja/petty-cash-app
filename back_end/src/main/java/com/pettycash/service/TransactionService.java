package com.pettycash.service;

import com.pettycash.entity.PendingTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pettycash.dto.TransactionDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;

import javassist.NotFoundException;

public interface TransactionService {
    Transaction addTransaction(TransactionDTO dto ,User user, TransactionType transactionType) throws NotFoundException;

    Page<Transaction> getAllWithPaging(Pageable pageable);

    boolean deleteTransaction(long transactionId);

    boolean updateTransaction(long transactionId, TransactionDTO dto, long userId) throws NotFoundException;

    boolean updateTransaction(long transactionId, PendingTransaction pendingTransaction, long userId) throws NotFoundException;

    Transaction getById(long transactionId);

    void updateTransactionImageName(Transaction transaction, String name);

    Page<Transaction> getAllByPendingDelete(String pendingDelete, Pageable pageable);

    void setPendingDeleteUpdate(long transactionId, String update);

}