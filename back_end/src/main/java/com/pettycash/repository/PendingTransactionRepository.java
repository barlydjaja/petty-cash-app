package com.pettycash.repository;

import com.pettycash.entity.NotApprovedTransaction;
import com.pettycash.entity.PendingTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingTransactionRepository extends JpaRepository<PendingTransaction, Long> {
    PendingTransaction getPendingTransactionByTransactionId(long transactionId);
    Page<PendingTransaction> findAllByOrderByTransactionDateAsc(Pageable pageable);
}
