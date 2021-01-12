package com.pettycash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pettycash.entity.TransactionType;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long>{

}
