package com.pettycash.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pettycash.en.Const;
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

    @Autowired
    public TransactionServiceImpl(TransactionRepository repo, UserService userService, TransactionTypeService typeService) {
        this.repo = repo;
        this.userService = userService;
        this.typeService = typeService;
    }

    public Transaction addTransaction(TransactionDTO dto, User user, TransactionType transactionType) {

        Transaction newTransaction = new Transaction();

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
        newTransaction.setResidue(user.getAccountBalance() + newTransaction.getAmount());
        userService.updateUserBalance(user.getUserId(), newTransaction.getAmount());

        return repo.save(newTransaction);
    }

    public List<Transaction> getAllByUser(User user) {
        return repo.findAllByUserOrderByTransactionDateAsc(user);
    }

    public LandingPageDTO getView(User user, List<Transaction> transaction) {
        LandingPageDTO result = new LandingPageDTO();
        result.setCode(user.getCode());
        result.setDepartment(user.getDepartment());
        result.setStartBalance(user.getStartBalance());
        result.setTransaction(transaction);
        result.setName(user.getUsername());

        long temp = user.getStartBalance();

        for (int i = 0; i < transaction.size(); i++) {
            result.getTransaction().get(i).setResidue(temp + result.getTransaction().get(i).getAmount());
            temp = result.getTransaction().get(i).getResidue();
        }

        result.setTotal(temp);

        return result;
    }

    private long toNegative(long amount) {
        return -amount;
    }

    public Page<Transaction> getAllWithPaging(User user, Pageable pageable) {
        if (repo.findByUserOrderByTransactionDateAsc(user, pageable) == null) {
            return null;
        }
        return repo.findByUserOrderByTransactionDateAsc(user, pageable);
    }

    public boolean deleteTransaction(long transactionId) {
        boolean result;

        try {
            User user = userService.getUserById(1);
            repo.delete(repo.getOne(transactionId));
            int flag = 0;
            Transaction indexMinus1 = null;
            List<Transaction> looping = repo.findAllByUserOrderByTransactionDateAsc(user);
            List<Transaction> update = new ArrayList<>();

            //get transaksi2 setelah index transaksi yang sudah dihapus
            for (int i = 0; i < looping.size(); i++) {
                if (looping.get(i).getTransactionId() > transactionId) {
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
            result = false;
        }

        return result;
    }

    public boolean updateTransaction(long transactionId, TransactionDTO dto, long userId) {

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

                User user = userService.getUserById(userId);

                List<Transaction> update = repo.findAllByUserOrderByTransactionDateAsc(user);
                List<Transaction> looping = new ArrayList<>();
                Transaction indexMinus1 = null;
                int flag = 0;
                for (int i = 0; i < update.size(); i++) {
                    if (update.get(i).getTransactionId() >= transaction.getTransactionId()) {
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
                            repo.save(transaction);
                        } else {
                            looping.get(i).setResidue(looping.get(i - 1).getResidue() + looping.get(i).getAmount());
                            repo.save(looping.get(i));
                        }
                    }
                } else { //update semua residue transaksi dimana index transaksi yg diupdate = yg pertama
                    for (int i = 0; i < looping.size(); i++) {
                        if (i == 0) {
                            transaction.setResidue(user.getStartBalance() + transaction.getAmount());
                            repo.save(transaction);
                        } else {
                            looping.get(i).setResidue(looping.get(i - 1).getResidue() + looping.get(i).getAmount());
                            repo.save(looping.get(i));
                        }
                    }
                }

                userService.updateUser(user, looping.get(looping.size() - 1).getResidue());
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
    public List<Transaction> getAllByUserAndIsApproved(User user, String isApproved) {
        return null;
    }

    @Override
    public Transaction approveTransaction(long transactionId) {
        Transaction transaction = repo.getOne(transactionId);
        if(transaction.getIsApproved().equalsIgnoreCase(Const.NOT_APPROVED)) {
            transaction.setIsApproved(Const.APPROVED);
        }
        repo.save(transaction);
        return transaction;
    }


}
