package com.pettycash.service;

import com.pettycash.dto.TransactionDTO;
import com.pettycash.entity.NotApprovedTransaction;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.User;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface NotApprovedTransactionService {
    NotApprovedTransaction addNotApprovedTransaction(TransactionDTO dto) throws NotFoundException;
    Transaction approveTransaction(User user, NotApprovedTransaction notApprovedTransaction) throws NotFoundException;
    Page<NotApprovedTransaction> getNotApprovedTransaction(Pageable pageable);
    NotApprovedTransaction findById(long id);
    void rejectNotApproved(long id);
    NotApprovedTransaction updateNotApprovedTransaction(Map<String, Object> request) throws NotFoundException;
}
