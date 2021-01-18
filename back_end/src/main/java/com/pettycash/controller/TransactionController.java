package com.pettycash.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pettycash.dto.GeneralResponse;
import com.pettycash.dto.TransactionDTO;
import com.pettycash.dto.TransactionTypeDTO;
import com.pettycash.dto.UserDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;
import com.pettycash.service.TransactionService;
import com.pettycash.service.TransactionTypeService;
import com.pettycash.service.UserService;

import javassist.NotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/v1/add")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionTypeService transactionTypeService;
    private final UserService userService;

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionTypeService transactionTypeService, UserService userService) {
        this.transactionService = transactionService;
        this.transactionTypeService = transactionTypeService;
        this.userService = userService;
    }

    @PostMapping("/addTransaction")
    @CrossOrigin
    public ResponseEntity<Transaction> addTransaction(@RequestBody TransactionDTO transactionDTO) throws NotFoundException {

        User user = userService.getUserById(transactionDTO.getUserId());
        TransactionType transactionType = transactionTypeService.getTypeById(transactionDTO.getTransactionTypeId());
        Transaction transaction = transactionService.addTransaction(transactionDTO, user, transactionType);

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/user")
    @CrossOrigin
    public ResponseEntity<User> addNewUser(@RequestBody UserDTO userDTO) throws Exception {
        User user = userService.addUser(userDTO);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/transactionType")
    @CrossOrigin
    public ResponseEntity<TransactionType> addNewTransactionType(@RequestBody TransactionTypeDTO name) {
        TransactionType type = transactionTypeService.addType(name.getName());

        return new ResponseEntity<>(type, HttpStatus.OK);
    }

    @GetMapping("/delete-transaction")
    @CrossOrigin
    public ResponseEntity<GeneralResponse> deleteTransaction(@RequestParam long transactionId, HttpServletRequest request) {
        HttpStatus status;
        GeneralResponse response = new GeneralResponse();
        boolean result = transactionService.deleteTransaction(transactionId);

        if(result){
            status = HttpStatus.OK;
        } else status = HttpStatus.INTERNAL_SERVER_ERROR;

        setResponse(response, result, request);

        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/update-transaction")
    @CrossOrigin
    public ResponseEntity<GeneralResponse> updateTransaction(@RequestParam long transactionId, @RequestBody TransactionDTO transactionDTO, HttpServletRequest request) throws NotFoundException {
        HttpStatus status;
        GeneralResponse response = new GeneralResponse();
        boolean result = transactionService.updateTransaction(transactionId, transactionDTO, transactionDTO.getUserId());

        if(result){
            status = HttpStatus.OK;
        } else status = HttpStatus.INTERNAL_SERVER_ERROR;

        setResponse(response, result, request);

        return new ResponseEntity<>(response, status);
    }

    @PreAuthorize("hasAuthority('ROLE_admin')")
    @GetMapping("/admin/approveTransaction")
    public ResponseEntity<Transaction> approveTransaction(@RequestParam long userId, @RequestParam long transactionId){
        Transaction transaction = transactionService.approveTransaction(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    private void setResponse(GeneralResponse response, boolean result, HttpServletRequest request) {
        if (result) {
            response.setPath(request.getRequestURI());
            response.setResponse("200");
            response.setMessage("SUCCESS");
            response.setDate(new Date());
        } else {
            response.setPath(request.getRequestURI());
            response.setResponse("500");
            response.setMessage("INTERNAL SERVER ERROR");
            response.setDate(new Date());
        }
    }

}
