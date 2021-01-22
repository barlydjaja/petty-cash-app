package com.pettycash.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.pettycash.en.Const;
import com.pettycash.entity.*;
import com.pettycash.exception.RequestNotAllowedException;
import com.pettycash.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import javassist.NotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionTypeService transactionTypeService;
    private final UserService userService;
    private final NotApprovedTransactionService notApprovedTransactionService;
    private final PendingTransactionService pendingTransactionService;

    @Autowired
    public TransactionController(PendingTransactionService pendingTransactionService, NotApprovedTransactionService notApprovedTransactionService, TransactionService transactionService, TransactionTypeService transactionTypeService, UserService userService) {
        this.transactionService = transactionService;
        this.transactionTypeService = transactionTypeService;
        this.userService = userService;
        this.notApprovedTransactionService = notApprovedTransactionService;
        this.pendingTransactionService = pendingTransactionService;
    }

    @PostMapping("/add")
    @CrossOrigin
    public ResponseEntity<?> addTransaction(@RequestBody TransactionDTO transactionDTO) throws NotFoundException {
        Transaction transaction;
        NotApprovedTransaction notApprovedTransaction;
        User user = userService.getUserById(transactionDTO.getUserId());
        TransactionType transactionType = transactionTypeService.getTypeById(transactionDTO.getTransactionTypeId());
        if(user.getRole().getRoleName().equalsIgnoreCase(Const.ADMIN)) {
            transaction = transactionService.addTransaction(transactionDTO, user, transactionType);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            notApprovedTransaction = notApprovedTransactionService.addNotApprovedTransaction(transactionDTO);
            return new ResponseEntity<>(notApprovedTransaction, HttpStatus.OK);
        }
    }

    @PostMapping("/user")
    @CrossOrigin
    public ResponseEntity<User> addNewUser(@RequestBody UserDTO userDTO) throws Exception {
        User user = userService.addUser(userDTO);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/type")
    @CrossOrigin
    public ResponseEntity<TransactionType> addNewTransactionType(@RequestBody TransactionTypeDTO name) {
        TransactionType type = transactionTypeService.addType(name.getName());

        return new ResponseEntity<>(type, HttpStatus.OK);
    }

    @GetMapping("/delete")
    @CrossOrigin
    public ResponseEntity<GeneralResponse> deleteTransaction(@RequestParam long userId, @RequestParam long transactionId, HttpServletRequest request) throws NotFoundException {
        HttpStatus status;
        boolean result = true;
        GeneralResponse response = new GeneralResponse();
        User user = userService.getUserById(userId);
        Transaction transaction = transactionService.getById(transactionId);
        if(user.getRole().getRoleName().equalsIgnoreCase(Const.ADMIN)) {
            result = transactionService.deleteTransaction(transactionId);
        } else {
            if(transaction.getPendingDelete().equalsIgnoreCase(Const.NO)){
                transactionService.setPendingDeleteUpdate(transactionId, Const.DELETE);
            }
        }

        if(result){
            status = HttpStatus.OK;
        } else status = HttpStatus.INTERNAL_SERVER_ERROR;

        setResponse(response, result, request);

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/approve-update")
    @CrossOrigin
    public ResponseEntity<GeneralResponse>  approveUpdateTransaction(@RequestParam long userId, @RequestParam long transactionId, HttpServletRequest request) throws NotFoundException, IOException {
        HttpStatus status;
        GeneralResponse response = new GeneralResponse();
        boolean result = false;
        Transaction transaction = transactionService.getById(transactionId);
        PendingTransaction pendingTransaction = pendingTransactionService.getByTransactionId(transactionId);
        User user = userService.getUserById(userId);
        if(user.getRole().getRoleName().equalsIgnoreCase(Const.ADMIN) && transaction.getPendingUpdate().equalsIgnoreCase(Const.YES)) {
            if (pendingTransaction != null) {
                result = transactionService.updateTransaction(transactionId, pendingTransaction, userId);
            } else throw new NotFoundException("pending transaction for transaction id : " + transactionId + " is not found");
        }

        if(result){
            status = HttpStatus.OK;
        } else status = HttpStatus.INTERNAL_SERVER_ERROR;

        setResponse(response, result, request);
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/update")
    @CrossOrigin
    public ResponseEntity<GeneralResponse> updateTransaction(@RequestParam long transactionId, @RequestBody TransactionDTO transactionDTO, HttpServletRequest request) throws NotFoundException {
        HttpStatus status;
        boolean result = true;
        GeneralResponse response = new GeneralResponse();
        Transaction transaction = transactionService.getById(transactionId);
        PendingTransaction pendingTransaction = pendingTransactionService.getByTransactionId(transactionId);

        User user = userService.getUserById(transactionDTO.getUserId());
              if (pendingTransaction != null) {
                throw new RequestNotAllowedException("update for transaction id : " + transactionId + " is not allowed, please check update request");
              }
            if(user.getRole().getRoleName().equalsIgnoreCase(Const.ADMIN)) {
                if (transaction.getPendingUpdate().equalsIgnoreCase(Const.NO)) {
                    result = transactionService.updateTransaction(transactionId, transactionDTO, user.getUserId());
                }
            }
            else if(transaction.getPendingUpdate().equalsIgnoreCase(Const.NO)) {
                    pendingTransactionService.savePendingTransaction(transactionId, transactionDTO);
                    transactionService.setPendingDeleteUpdate(transactionId, Const.UPDATE);
            }

        if(result){
            status = HttpStatus.OK;
        } else status = HttpStatus.INTERNAL_SERVER_ERROR;

        setResponse(response, result, request);

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/approve")
    @CrossOrigin
    public ResponseEntity<Transaction> approveTransaction(@RequestParam long userId, @RequestParam long transactionId) throws NotFoundException {
        User user = userService.getUserById(userId);
        NotApprovedTransaction notApprovedTransaction = notApprovedTransactionService.findById(transactionId);
        Transaction transaction = notApprovedTransactionService.approveTransaction(user, notApprovedTransaction);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/edit-pending")
    @CrossOrigin
    public ResponseEntity<PendingTransaction> editPendingTransaction(@RequestBody Map<String, Object> request) throws NotFoundException {
        /*
            {
                "pendingTransactionId" : long,
                "transactionTypeId" : long,
                "amount" : long,
                "description" : String,
                "receipt" : String,
            }
         */
        PendingTransaction pendingTransaction = pendingTransactionService.updatePendingTransaction(request);

        return new ResponseEntity<>(pendingTransaction, HttpStatus.OK);
    }

    @PostMapping("/update-approve")
    @CrossOrigin
    public ResponseEntity<NotApprovedTransaction> updateNotApprovedTransaction(@RequestBody Map<String, Object> request) throws NotFoundException {
        /*
            {
                "notTransactionId" : long,
                "transactionTypeId" : long,
                "amount" : long,
                "description" : String,
                "receipt" : String,
            }
         */
        NotApprovedTransaction notApprovedTransaction = notApprovedTransactionService.updateNotApprovedTransaction(request);

        return new ResponseEntity<>(notApprovedTransaction, HttpStatus.OK);
    }

    @GetMapping("/reject-approve")
    @CrossOrigin
    public ResponseEntity<GeneralResponse> rejectApprove(@RequestParam long transactionId, HttpServletRequest request) {
        GeneralResponse response = new GeneralResponse();
        HttpStatus status;
        boolean result;
        try {
            notApprovedTransactionService.rejectNotApproved(transactionId);
            result = true;
        } catch (Exception e){
            result = false;
            System.out.println(e.getLocalizedMessage());
        }

        if(result){
            status = HttpStatus.OK;
        } else status = HttpStatus.INTERNAL_SERVER_ERROR;

        setResponse(response, result, request);

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/reject-delete")
    @CrossOrigin
    public ResponseEntity<Transaction> rejectDelete(@RequestParam long transactionId) throws NotFoundException {
        Transaction transaction = transactionService.getById(transactionId);
        if(transaction == null){
            throw new NotFoundException("transaction not found for id : " + transactionId);
        }
        if(transaction.getPendingDelete().equalsIgnoreCase(Const.YES)) {
            transactionService.setPendingDeleteUpdate(transactionId, Const.DELETE);
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/reject-update")
    @CrossOrigin
    public ResponseEntity<Transaction> rejectUpdate(@RequestParam long transactionId){
        PendingTransaction pendingTransaction = pendingTransactionService.getByTransactionId(transactionId);
        Transaction transaction = transactionService.getById(pendingTransaction.getTransactionId());
        if(transaction.getPendingUpdate().equalsIgnoreCase(Const.YES)){
            pendingTransactionService.deletePendingTransaction(pendingTransaction.getTransactionId());
            transactionService.setPendingDeleteUpdate(transactionId, Const.UPDATE);
        }
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
