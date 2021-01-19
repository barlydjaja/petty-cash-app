package com.pettycash.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pettycash.en.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pettycash.dto.LandingPageDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;
import com.pettycash.service.TransactionService;
import com.pettycash.service.TransactionTypeService;
import com.pettycash.service.UserService;

import javassist.NotFoundException;

import javax.xml.ws.Response;

@CrossOrigin
@RestController
@RequestMapping("/v1/view")
public class ViewController {

    private final TransactionService transactionService;
    private final TransactionTypeService transactionTypeService;
    private final UserService userService;

    @Autowired
    public ViewController(TransactionService transactionService, TransactionTypeService transactionTypeService, UserService userService) {
        this.transactionService = transactionService;
        this.transactionTypeService = transactionTypeService;
        this.userService = userService;
    }

    @GetMapping("/getTransactionType")
    @CrossOrigin
    public ResponseEntity<List<TransactionType>> getTransactionType() {
        List<TransactionType> result = transactionTypeService.getAllTransactionType();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getTransaction")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> getTransactionByUserIdWithPaging(@RequestParam("userId") long userId, @RequestParam int page) throws NotFoundException {

        System.out.println("get trans");
        List<Transaction> transactions;
        User user = userService.getUserById(userId);
        Pageable paging = PageRequest.of(page, 10);

        Page<Transaction> pageTrans = transactionService.getAllWithPaging(user, paging);

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

    @GetMapping("/approved-transaction")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> getApprovedTransactionWithPaging(@RequestParam("userId") long userId, @RequestParam int page) throws NotFoundException {
        List<Transaction> transactions;
        Pageable paging = PageRequest.of(page, 10);

        User user = userService.getUserById(userId);
        Page<Transaction> pageTrans = transactionService.getTransactionByIsApproved(Const.APPROVED, paging);
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

    @GetMapping("/not-approved-transaction")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> getNotApprovedTransactionWithPaging(@RequestParam("userId") long userId, @RequestParam int page) throws NotFoundException {
        List<Transaction> transactions;
        Pageable paging = PageRequest.of(page, 10);

        User user = userService.getUserById(userId);
        Page<Transaction> pageTrans = transactionService.getTransactionByIsApproved(Const.NOT_APPROVED, paging);
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

    @PostMapping("/anjink")
    public ResponseEntity<User> anjing(@RequestBody Map<String, String> request){
        User user = userService.getUserByUsername(request.get("username"));
        return new ResponseEntity<>(userService.changePassword(user.getUserId(), request.get("password")), HttpStatus.OK);
    }
}
