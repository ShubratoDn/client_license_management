package com.license.management.exceptions;

public class TransactionFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionFailedException(String licenseId) {
		super("Transaction failed during work with License " + licenseId);
	}
	
}
