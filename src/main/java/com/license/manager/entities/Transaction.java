package com.license.manager.entities;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private License license;
	
	private Timestamp transactionDate;
	
	private String transactionType;
	
	private double amount;
	
	private String paymentStatus;
	
}
