package com.pettycash.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pending_transaction")
@Getter
@Setter
public class PendingTransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pending_transaction_id")
    private long pendingTransactionId;

    @Column(name = "transaction_id")
    private long transactionId;

    @Column(name = "description")
    private String description;

    @Column(name = "receipt") //pengeluaran or pemasukan
    private String receipt;

    @Column(name = "amount")
    private long amount;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "transaction_type_id")
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "picture_name")
    private String fileName;
}
