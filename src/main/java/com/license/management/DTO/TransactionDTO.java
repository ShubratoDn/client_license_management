package com.license.management.DTO;

import java.sql.Timestamp;

import com.license.management.entities.License;
import com.license.management.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
	private Long transactionId;
	
	private User user;
	
	private License license;
	
	private Timestamp transactionDate;
	
	private String transactionType;
	
	private double amount;
	
	private String paymentStatus;
}
