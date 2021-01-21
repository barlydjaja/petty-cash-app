package com.pettycash.repository;

import com.pettycash.entity.NotApprovedTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotApprovedTransactionRepository extends JpaRepository<NotApprovedTransaction, Long> {
    Page<NotApprovedTransaction> findAllByOrderByTransactionDateAsc(Pageable pageable);
}
