package com.pettycash.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "not_approved_transactions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NotApprovedTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_approved_transaction_id")
    private long notTransactionId;

    @Column(name = "not_approved_description")
    private String description;

    @Column(name = "not_approved_receipt") //pengeluaran or pemasukan
    private String receipt;

    @Column(name = "not_approved_amount")
    private long amount;

    @Column(name = "not_approved_transaction_date")
    private Date transactionDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "transaction_type_id")
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "not_approved_picture_name")
    private String fileName;
}
