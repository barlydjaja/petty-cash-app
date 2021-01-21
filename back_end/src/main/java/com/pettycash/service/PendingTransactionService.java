package com.pettycash.service;

import com.pettycash.dto.TransactionDTO;
import com.pettycash.entity.PendingTransaction;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PendingTransactionService {
    void savePendingTransaction(long transactionId, TransactionDTO transaction) throws NotFoundException;
    PendingTransaction getByTransactionId(long transactionId);
    void deletePendingTransaction(long transactionId);
    Page<PendingTransaction> getPendingUpdateTransactions(Pageable pageable);
    PendingTransaction updatePendingTransaction(Map<String, Object> request) throws NotFoundException;
}
