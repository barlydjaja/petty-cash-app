package com.pettycash.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pettycash.en.Const;
import com.pettycash.entity.PendingTransaction;
import com.pettycash.repository.PendingTransactionRepository;
import com.pettycash.service.PendingTransactionService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pettycash.dto.LandingPageDTO;
import com.pettycash.dto.TransactionDTO;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.TransactionType;
import com.pettycash.entity.User;
import com.pettycash.repository.TransactionRepository;
import com.pettycash.service.TransactionService;
import com.pettycash.service.TransactionTypeService;
import com.pettycash.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repo;
    private final UserService userService;
    private final TransactionTypeService typeService;
    private final PendingTransactionService pendingTransactionService;

    @Autowired
    public TransactionServiceImpl(PendingTransactionService pendingTransactionService, TransactionRepository repo, UserService userService, TransactionTypeService typeService) {
        this.repo = repo;
        this.userService = userService;
        this.typeService = typeService;
        this.pendingTransactionService = pendingTransactionService;
    }

    @Override
    public Transaction addTransaction(TransactionDTO dto, User user, TransactionType transactionType) throws NotFoundException {

        Transaction newTransaction = new Transaction();
        User admin = userService.getUserById(1);

        List<Transaction> transactions = repo.findAllByOrderByTransactionDateAsc();
        List<Transaction> transactionsAfterNewTransactions = new ArrayList<>();
        Transaction beforeTransaction = null;

        if (dto.getReceipt().equalsIgnoreCase(Const.INCOME)) {
            newTransaction.setReceipt(Const.INCOME);
            newTransaction.setAmount(dto.getAmount());
        } else {
            newTransaction.setReceipt(Const.OUTCOME);
            newTransaction.setAmount(toNegative(dto.getAmount()));
        }
        newTransaction.setPendingUpdate(Const.NO);
        newTransaction.setPendingDelete(Const.NO);
        newTransaction.setDescription(dto.getDescription());
        newTransaction.setUser(user);
        newTransaction.setTransactionType(transactionType);
        if(dto.getDate() == null) { //transaksi yg dibuat admin langsung
            newTransaction.setTransactionDate(new Date());
            newTransaction.setResidue(admin.getAccountBalance() + newTransaction.getAmount());
            userService.updateUserBalance(admin.getUserId(), newTransaction.getAmount());
        } else { //transaksi yg dibuat oleh staff dan di approve
            newTransaction.setTransactionDate(dto.getDate());
            int flag =0;
            for(int i =0 ; i < transactions.size(); i++){
                if(newTransaction.getTransactionDate().before(transactions.get(i).getTransactionDate())){
                    if(flag == 0 && i > 0){
                        beforeTransaction = transactions.get(i-1);
                        flag = 1;
                        newTransaction.setResidue(beforeTransaction.getResidue() + newTransaction.getAmount());
                        transactionsAfterNewTransactions.add(transactions.get(i));
                    } else {
                        transactionsAfterNewTransactions.add(transactions.get(i));
                        flag = 1;
                    }
                }
            }

            if(beforeTransaction == null){
                newTransaction.setResidue(user.getStartBalance() + newTransaction.getAmount());
            }

            if(!transactionsAfterNewTransactions.isEmpty()) {
                for (int i = 0; i < transactionsAfterNewTransactions.size(); i++) {
                    if (i == 0) {
                        transactionsAfterNewTransactions.get(i).setResidue(newTransaction.getResidue() + transactionsAfterNewTransactions.get(i).getAmount());
                    } else {
                        transactionsAfterNewTransactions.get(i).setResidue(transactionsAfterNewTransactions.get(i-1).getResidue() + transactionsAfterNewTransactions.get(i).getAmount());
                    }
                    repo.save(transactionsAfterNewTransactions.get(i));

                    if(i == transactionsAfterNewTransactions.size() - 1){
                        userService.updateUser(admin, transactionsAfterNewTransactions.get(i).getResidue()); //set admin account balance
                    }
                }

            } else {
                newTransaction.setResidue(admin.getAccountBalance() + newTransaction.getAmount());
                userService.updateUserBalance(admin.getUserId(), newTransaction.getAmount());
            }

        }
        return repo.save(newTransaction);
    }

    @Override
    public Page<Transaction> getAllWithPaging(Pageable pageable) {
        if (repo.findByOrderByTransactionDateAsc(pageable) == null) {
            return null;
        }
        return repo.findByOrderByTransactionDateAsc(pageable);
    }

    @Override
    public boolean deleteTransaction(long transactionId) {
        boolean result = false;

        try {
            User user = userService.getUserById(1);
            Transaction transaction = repo.getOne(transactionId);
            repo.delete(repo.getOne(transactionId));

                int flag = 0;
                Transaction indexMinus1 = null;
                List<Transaction> looping = repo.findAllByOrderByTransactionDateAsc();
                List<Transaction> update = new ArrayList<>();

                //get transaksi2 setelah index transaksi yang sudah dihapus
                for (int i = 0; i < looping.size(); i++) {
                    if (looping.get(i).getTransactionDate().after(transaction.getTransactionDate())) {
                        if (flag == 0) {
                            if (i > 0) {
                                indexMinus1 = looping.get(i - 1); //get 1 transaksi sebelum index transaksi yg dihapus
                            }
                            update.add(looping.get(i));
                            flag = 1;
                        } else update.add(looping.get(i));
                    }
                }

                if (indexMinus1 != null) { //looping kalo transaksi yg dihapus bukan index pertama
                    for (int i = 0; i < update.size(); i++) {
                        if (i == 0) {
                            update.get(i).setResidue(indexMinus1.getResidue() + update.get(i).getAmount());
                        } else {
                            update.get(i).setResidue(update.get(i - 1).getResidue() + update.get(i).getAmount());
                        }
                        repo.save(update.get(i));
                        if (i == update.size() - 1) {
                            userService.updateUser(user, update.get(i).getResidue());
                        }
                    }
                } else { //looping kalo transaksi yg dihapus ternyata index pertama
                    for (int i = 0; i < update.size(); i++) {
                        if (i == 0) {
                            update.get(i).setResidue(user.getStartBalance() + update.get(i).getAmount());
                        } else {
                            update.get(i).setResidue(update.get(i - 1).getResidue() + update.get(i).getAmount());
                        }
                            repo.save(update.get(i));
                            if (i == update.size() - 1) {
                                userService.updateUser(user, update.get(i).getResidue());
                            }
                    }
                }

                //no loop karena transaksi yang dihapus ternyata index terakhir
                if (update.isEmpty()) {
                    userService.updateUser(user, looping.get(looping.size() - 1).getResidue());
                }
                result = true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return result;
    }

    @Override
    public boolean updateTransaction(long transactionId, TransactionDTO dto, long userId) throws NotFoundException {

        User admin = userService.getUserById(1);

        boolean result;

        try {
            Transaction transaction = repo.getOne(transactionId);

            if (!dto.getDescription().equalsIgnoreCase(transaction.getDescription())) {
                transaction.setDescription(dto.getDescription());
            }

            TransactionType dtoType = typeService.getTypeById(dto.getTransactionTypeId());

            if (transaction.getTransactionType() != dtoType) {
                transaction.setTransactionType(dtoType);
            }

            if (dto.getAmount() != transaction.getAmount() || !dto.getReceipt().equalsIgnoreCase(transaction.getReceipt())) {
                if (dto.getReceipt().equalsIgnoreCase("outcome")) {
                    transaction.setAmount(-dto.getAmount());
                } else transaction.setAmount(dto.getAmount());

                transaction.setReceipt(dto.getReceipt());


                List<Transaction> update = repo.findAllByOrderByTransactionDateAsc();
                List<Transaction> looping = new ArrayList<>();
                Transaction indexMinus1 = null;
                int flag = 0;
                for (int i = 0; i < update.size(); i++) {
                    if (update.get(i).getTransactionDate().compareTo(transaction.getTransactionDate()) >= 0) {
                        if (i == 0) { //yg diupdate index pertama transaksi
                            looping.add(transaction);
                            flag = 1;
                        } else if (flag == 0) {
                            indexMinus1 = update.get(i - 1);
                            looping.add(transaction);
                            flag = 1;
                        } else {
                            looping.add(update.get(i));
                        }
                    }
                }

                    if (indexMinus1 != null) { //update semua residue transaksi dimana index transaksi yg diupdate bukan yg pertama
                        for (int i = 0; i < looping.size(); i++) {
                            if (i == 0) {
                                transaction.setResidue(indexMinus1.getResidue() + transaction.getAmount());
                                transaction.setPendingUpdate(Const.NO);
                                repo.save(transaction);
                            } else {
                                looping.get(i).setResidue(looping.get(i - 1).getResidue() + looping.get(i).getAmount());
                                repo.save(looping.get(i));
                            }
                        }
                    } else { //update semua residue transaksi dimana index transaksi yg diupdate = yg pertama
                        for (int i = 0; i < looping.size(); i++) {
                            if (i == 0) {
                                transaction.setResidue(admin.getStartBalance() + transaction.getAmount());
                                transaction.setPendingUpdate(Const.NO);
                                repo.save(transaction);
                            } else {
                                looping.get(i).setResidue(looping.get(i - 1).getResidue() + looping.get(i).getAmount());
                                repo.save(looping.get(i));
                            }
                        }
                    }
                    userService.updateUser(admin, looping.get(looping.size() - 1).getResidue());
                }
            result = true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            result = false;
        }
        return result;
    }

    @Override
    public boolean updateTransaction(long transactionId, PendingTransaction pendingTransaction, long userId) throws NotFoundException {
        User admin = userService.getUserById(1);
        TransactionDTO dto = new TransactionDTO();

            pendingTransaction = pendingTransactionService.getByTransactionId(transactionId);
            dto.setDate(pendingTransaction.getTransactionDate());
            dto.setUserId(pendingTransaction.getUser().getUserId());
            dto.setAmount(pendingTransaction.getAmount());
            dto.setReceipt(pendingTransaction.getReceipt());
            dto.setDescription(pendingTransaction.getDescription());
            dto.setTransactionTypeId(pendingTransaction.getTransactionType().getTransactionTypeId());

            pendingTransactionService.deletePendingTransaction(pendingTransaction.getPendingTransactionId());

        boolean result;

        try {
            Transaction transaction = repo.getOne(transactionId);

            if (!dto.getDescription().equalsIgnoreCase(transaction.getDescription())) {
                transaction.setDescription(dto.getDescription());
            }

            TransactionType dtoType = typeService.getTypeById(dto.getTransactionTypeId());

            if (transaction.getTransactionType() != dtoType) {
                transaction.setTransactionType(dtoType);
            }

            if (dto.getAmount() != transaction.getAmount() || !dto.getReceipt().equalsIgnoreCase(transaction.getReceipt())) {
                if (dto.getReceipt().equalsIgnoreCase("outcome")) {
                    transaction.setAmount(-dto.getAmount());
                } else transaction.setAmount(dto.getAmount());

                transaction.setReceipt(dto.getReceipt());


                List<Transaction> update = repo.findAllByOrderByTransactionDateAsc();
                List<Transaction> looping = new ArrayList<>();
                Transaction indexMinus1 = null;
                int flag = 0;
                for (int i = 0; i < update.size(); i++) {
                    if (update.get(i).getTransactionDate().compareTo(transaction.getTransactionDate()) >= 0) {
                        if (i == 0) { //yg diupdate index pertama transaksi
                            looping.add(transaction);
                            flag = 1;
                        } else if (flag == 0) {
                            indexMinus1 = update.get(i - 1);
                            looping.add(transaction);
                            flag = 1;
                        } else {
                            looping.add(update.get(i));
                        }
                    }
                }

                if (indexMinus1 != null) { //update semua residue transaksi dimana index transaksi yg diupdate bukan yg pertama
                    for (int i = 0; i < looping.size(); i++) {
                        if (i == 0) {
                            transaction.setResidue(indexMinus1.getResidue() + transaction.getAmount());
                            transaction.setPendingUpdate(Const.NO);
                            repo.save(transaction);
                        } else {
                            looping.get(i).setResidue(looping.get(i - 1).getResidue() + looping.get(i).getAmount());
                            repo.save(looping.get(i));
                        }
                    }
                } else { //update semua residue transaksi dimana index transaksi yg diupdate = yg pertama
                    for (int i = 0; i < looping.size(); i++) {
                        if (i == 0) {
                            transaction.setResidue(admin.getStartBalance() + transaction.getAmount());
                            transaction.setPendingUpdate(Const.NO);
                            repo.save(transaction);
                        } else {
                            looping.get(i).setResidue(looping.get(i - 1).getResidue() + looping.get(i).getAmount());
                            repo.save(looping.get(i));
                        }
                    }
                }
                userService.updateUser(admin, looping.get(looping.size() - 1).getResidue());
            }
            result = true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            result = false;
        }
        return result;
    }

    @Override
    public Transaction getById(long transactionId) {
        return repo.getOne(transactionId);
    }

    @Override
    public void updateTransactionImageName(Transaction transaction, String name) {
        transaction.setFileName(name);
        repo.save(transaction);
    }

    @Override
    public Page<Transaction> getAllByPendingDelete(String pendingDelete, Pageable pageable) {
        return repo.findAllByPendingDeleteOrderByTransactionDateAsc(pendingDelete, pageable);
    }

    @Override
    public void setPendingDeleteUpdate(long transactionId, String update) {
        Transaction transaction = repo.getOne(transactionId);
        if(update.equalsIgnoreCase(Const.UPDATE)){
            if(transaction.getPendingUpdate().equalsIgnoreCase(Const.NO)) {
                transaction.setPendingUpdate(Const.YES);
            } else transaction.setPendingUpdate(Const.NO);
        } else if(update.equalsIgnoreCase(Const.DELETE)){
            if(transaction.getPendingDelete().equalsIgnoreCase(Const.NO)) {
                transaction.setPendingDelete(Const.YES);
            } else transaction.setPendingDelete(Const.NO);
        }
        repo.save(transaction);
    }

    private long toNegative(long amount) {
        return -amount;
    }

}
