package com.pettycash.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pettycash.en.Const;
import com.pettycash.entity.*;
import com.pettycash.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javassist.NotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/v1/view")
public class ViewController {

    private final TransactionService transactionService;
    private final TransactionTypeService transactionTypeService;
    private final UserService userService;
    private final NotApprovedTransactionService notApprovedTransactionService;
    private final PendingTransactionService pendingTransactionService;

    @Autowired
    public ViewController(PendingTransactionService pendingTransactionService, NotApprovedTransactionService notApprovedTransactionService, TransactionService transactionService, TransactionTypeService transactionTypeService, UserService userService) {
        this.transactionService = transactionService;
        this.transactionTypeService = transactionTypeService;
        this.userService = userService;
        this.notApprovedTransactionService = notApprovedTransactionService;
        this.pendingTransactionService = pendingTransactionService;
    }

    @GetMapping("/getTransactionType")
    @CrossOrigin
    public ResponseEntity<List<TransactionType>> getTransactionType() {
        List<TransactionType> result = transactionTypeService.getAllTransactionType();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/approved-transaction")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> getApprovedTransactionWithPaging(@RequestParam("userId") long userId, @RequestParam int page) throws NotFoundException {
        List<Transaction> transactions;
        Pageable paging = PageRequest.of(page, 10);

        User user = userService.getUserById(userId);
        User admin = userService.getUserById(Const.ADMIN_ID);
        Page<Transaction> pageTrans = transactionService.getAllWithPaging(paging);
        transactions = pageTrans.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getUsername());
        response.put("code", user.getCode());
        response.put("department", user.getDepartment());
        response.put("transactions", transactions);
        response.put("currentPage", pageTrans.getNumber());
        response.put("totalItems", pageTrans.getTotalElements());
        response.put("totalPages", pageTrans.getTotalPages());
        response.put("totalBalance", admin.getAccountBalance());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/not-approved-transaction")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> getNotApprovedTransactionWithPaging(@RequestParam("userId") long userId, @RequestParam int page) throws NotFoundException {
        List<NotApprovedTransaction> notApprovedTransactions;
        Pageable paging = PageRequest.of(page, 10);

        User user = userService.getUserById(userId);
        Page<NotApprovedTransaction> pageTrans = notApprovedTransactionService.getNotApprovedTransaction(paging);
        notApprovedTransactions = pageTrans.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getUsername());
        response.put("code", user.getCode());
        response.put("department", user.getDepartment());
        response.put("transactions", notApprovedTransactions);
        response.put("currentPage", pageTrans.getNumber());
        response.put("totalItems", pageTrans.getTotalElements());
        response.put("totalPages", pageTrans.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/pending-update")
    public ResponseEntity<Map<String, Object>> getPendingUpdateTransactions(@RequestParam("userId") long userId, @RequestParam int page) throws NotFoundException {
        List<PendingTransaction> pendingTransactions;
        Pageable pageable = PageRequest.of(page, 10);

        User user = userService.getUserById(userId);
        Page<PendingTransaction> pageTrans = pendingTransactionService.getPendingUpdateTransactions(pageable);

        pendingTransactions = pageTrans.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getUsername());
        response.put("code", user.getCode());
        response.put("department", user.getDepartment());
        response.put("transactions", pendingTransactions);
        response.put("currentPage", pageTrans.getNumber());
        response.put("totalItems", pageTrans.getTotalElements());
        response.put("totalPages", pageTrans.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/pending-delete")
    public ResponseEntity<Map<String, Object>> getPendingDeleteTransactions(@RequestParam("userId") long userId, @RequestParam int page) throws NotFoundException {
        List<Transaction> transactions;
        Pageable pageable = PageRequest.of(page, 10);

        User user = userService.getUserById(userId);
        Page<Transaction> pageTrans = transactionService.getAllByPendingDelete(Const.YES, pageable);

        transactions = pageTrans.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getUsername());
        response.put("code", user.getCode());
        response.put("department", user.getDepartment());
        response.put("transactions", transactions);
        response.put("currentPage", pageTrans.getNumber());
        response.put("totalItems", pageTrans.getTotalElements());
        response.put("totalPages", pageTrans.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
