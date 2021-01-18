package com.pettycash.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "transaction")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transaction implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
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
	
	@Column(name = "residue")
	private long residue;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "picture_name")
	private String fileName;

	@Column(name = "is_approved", columnDefinition = "varchar(50) default 'not_approved'")
	private String isApproved;
	
}
