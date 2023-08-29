package com.license.manager.DTO;

import java.sql.Timestamp;

import com.license.manager.entities.License;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionHistoryDto {
	private Long transactionId;
	
	private String username;
	
	private License license;
	
	private Timestamp transactionDate;
	
	private String transactionType;
	
	private double amount;
	
	private String paymentStatus;
}
