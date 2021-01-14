package com.pettycash.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "department")
	private String department;
	
	@Column(name = "balance")
	private long startBalance;
	
	@Column(name = "user_name")
	private String username;
	
	@Column(name = "account_balance")
	private long accountBalance;

	@Column(name = "role")
	private String role;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	public User(User user) {
	}

	public User() {

	}
}
