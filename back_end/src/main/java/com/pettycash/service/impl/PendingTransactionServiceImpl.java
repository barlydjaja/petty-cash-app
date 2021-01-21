package com.pettycash.service.impl;

import com.pettycash.dto.TransactionDTO;
import com.pettycash.entity.PendingTransaction;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.repository.PendingTransactionRepository;
import com.pettycash.service.PendingTransactionService;
import com.pettycash.service.TransactionService;
import com.pettycash.service.TransactionTypeService;
import com.pettycash.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PendingTransactionServiceImpl implements PendingTransactionService {

    private final PendingTransactionRepository pendingTransactionRepository;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionTypeService transactionTypeService;

    @Autowired
    public PendingTransactionServiceImpl(@Lazy TransactionService transactionService, TransactionTypeService transactionTypeService, UserService userService, PendingTransactionRepository pendingTransactionRepository){
        this.pendingTransactionRepository = pendingTransactionRepository;
        this.userService = userService;
        this.transactionService = transactionService;
        this.transactionTypeService = transactionTypeService;
    }

    @Override
    public void savePendingTransaction(long transactionId, TransactionDTO transaction) throws NotFoundException {

        Transaction existing = transactionService.getById(transactionId);

        PendingTransaction pendingTransaction = new PendingTransaction();
        pendingTransaction.setTransactionId(transactionId);
        pendingTransaction.setAmount(Math.abs(transaction.getAmount()));
        pendingTransaction.setReceipt(transaction.getReceipt());
        pendingTransaction.setDescription(transaction.getDescription());
        pendingTransaction.setUser(userService.getUserById(transaction.getUserId()));
        pendingTransaction.setTransactionDate(existing.getTransactionDate());
        pendingTransaction.setTransactionType(transactionTypeService.getTypeById(transaction.getTransactionTypeId()));

        pendingTransactionRepository.save(pendingTransaction);
    }

    @Override
    public PendingTransaction getByTransactionId(long transactionId) {
        return pendingTransactionRepository.getPendingTransactionByTransactionId(transactionId);
    }

    @Override
    public void deletePendingTransaction(long transactionId) {
        PendingTransaction pendingTransaction = getByTransactionId(transactionId);
        pendingTransactionRepository.delete(pendingTransaction);
    }

    @Override
    public Page<PendingTransaction> getPendingUpdateTransactions(Pageable pageable) {
        return pendingTransactionRepository.findAllByOrderByTransactionDateAsc(pageable);
    }

    @Override
    public PendingTransaction updatePendingTransaction(Map<String, Object> request) throws NotFoundException {

        TransactionType transactionType = transactionTypeService.getTypeById(Long.parseLong(String.valueOf(request.get("transactionTypeId"))));
        PendingTransaction pendingTransaction = pendingTransactionRepository.getOne(Long.parseLong(String.valueOf(request.get("pendingTransactionId"))));
        pendingTransaction.setAmount(Long.parseLong(String.valueOf(request.get("amount"))));
        pendingTransaction.setDescription(String.valueOf(request.get("description")));
        pendingTransaction.setReceipt(String.valueOf(request.get("receipt")));
        pendingTransaction.setTransactionType(transactionType);

        return pendingTransactionRepository.save(pendingTransaction);
    }
}
