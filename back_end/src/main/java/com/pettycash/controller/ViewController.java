package com.pettycash.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pettycash.dto.LandingPageDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;
import com.pettycash.service.TransactionService;
import com.pettycash.service.TransactionTypeService;
import com.pettycash.service.UserService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/view")
public class ViewController {

    private final TransactionService transactionService;
    private final TransactionTypeService transactionTypeService;
    private final UserService userService;

    public ViewController(TransactionService transactionService, TransactionTypeService transactionTypeService, UserService userService) {
        this.transactionService = transactionService;
        this.transactionTypeService = transactionTypeService;
        this.userService = userService;
    }

    @GetMapping("/getAllByUserId")
    @CrossOrigin
    public ResponseEntity<LandingPageDTO> getTransactionByUserId(@RequestParam("userId") long userId) throws NotFoundException {
        User user = userService.getUserById(userId);
        List<Transaction> transaction = transactionService.getAllByUser(user);
        LandingPageDTO view = transactionService.getView(user, transaction);

        return new ResponseEntity<>(view, HttpStatus.OK);
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
        List<Transaction> transactions;
        User user = userService.getUserById(userId);
        Pageable paging = PageRequest.of(page, 10);

        Page<Transaction> pageTrans = transactionService.getAllWithPaging(user, paging);

        transactions = pageTrans.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getName());
        response.put("code", user.getCode());
        response.put("department", user.getDepartment());
        response.put("transactions", transactions);
        response.put("currentPage", pageTrans.getNumber());
        response.put("totalItems", pageTrans.getTotalElements());
        response.put("totalPages", pageTrans.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
