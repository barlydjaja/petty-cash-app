package com.pettycash.service.impl;

import com.pettycash.dto.TransactionDTO;
import com.pettycash.en.Const;
import com.pettycash.entity.NotApprovedTransaction;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;
import com.pettycash.exception.RequestNotAllowedException;
import com.pettycash.repository.NotApprovedTransactionRepository;
import com.pettycash.service.NotApprovedTransactionService;
import com.pettycash.service.TransactionService;
import com.pettycash.service.TransactionTypeService;
import com.pettycash.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotApprovedTransactionServiceImpl implements NotApprovedTransactionService {

    private final NotApprovedTransactionRepository notApprovedTransactionRepository;
    private final TransactionTypeService transactionTypeService;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public NotApprovedTransactionServiceImpl(@Lazy TransactionService transactionService, NotApprovedTransactionRepository notApprovedTransactionRepository, TransactionTypeService transactionTypeService, UserService userService){
        this.notApprovedTransactionRepository = notApprovedTransactionRepository;
        this.transactionTypeService = transactionTypeService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @Override
    public NotApprovedTransaction addNotApprovedTransaction(TransactionDTO dto) throws NotFoundException {

        TransactionType transactionType = transactionTypeService.getTypeById(dto.getTransactionTypeId());

        User user = userService.getUserById(dto.getUserId());

        NotApprovedTransaction newTransaction = new NotApprovedTransaction();

        if (dto.getReceipt().equalsIgnoreCase(Const.INCOME)) {
            newTransaction.setReceipt(Const.INCOME);
            newTransaction.setAmount(dto.getAmount());
        } else {
            newTransaction.setReceipt(Const.OUTCOME);
            newTransaction.setAmount(toNegative(dto.getAmount()));
        }

        newTransaction.setDescription(dto.getDescription());
        newTransaction.setUser(user);
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionType(transactionType);
        return notApprovedTransactionRepository.save(newTransaction);
    }

    @Override
    public Transaction approveTransaction(User user, NotApprovedTransaction notApprovedTransaction) throws NotFoundException {
        if(user.getRole().getRoleName().equalsIgnoreCase(Const.STAFF)){
            throw new RequestNotAllowedException("user not eligible to approve");
        }
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionTypeId(notApprovedTransaction.getTransactionType().getTransactionTypeId());
        transactionDTO.setReceipt(notApprovedTransaction.getReceipt());
        transactionDTO.setUserId(notApprovedTransaction.getUser().getUserId());
        transactionDTO.setAmount(Math.abs(notApprovedTransaction.getAmount()));
        transactionDTO.setDate(notApprovedTransaction.getTransactionDate());
        transactionDTO.setDescription(notApprovedTransaction.getDescription());
        Transaction transaction = transactionService.addTransaction(transactionDTO, notApprovedTransaction.getUser(), notApprovedTransaction.getTransactionType());
        notApprovedTransactionRepository.delete(notApprovedTransaction);
        return transaction;
    }

    @Override
    public Page<NotApprovedTransaction> getNotApprovedTransaction(Pageable pageable) {
        if (notApprovedTransactionRepository.findAllByOrderByTransactionDateAsc(pageable) == null) {
            return null;
        }
        return notApprovedTransactionRepository.findAllByOrderByTransactionDateAsc(pageable);
    }

    @Override
    public NotApprovedTransaction findById(long id) {
        return notApprovedTransactionRepository.getOne(id);
    }

    private long toNegative(long amount) {
        return -amount;
    }
}
