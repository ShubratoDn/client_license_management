package com.license.management.DTO;

import java.sql.Timestamp;

import com.license.management.entities.License;

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
