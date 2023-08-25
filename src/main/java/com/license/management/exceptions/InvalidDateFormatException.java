package com.license.management.exceptions;

public class InvalidDateFormatException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidDateFormatException() {
        super("Invalid date format. Please provide a date in the format yyyy-MM-dd.");
    }
}