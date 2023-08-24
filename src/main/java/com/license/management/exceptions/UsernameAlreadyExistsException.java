package com.license.management.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsernameAlreadyExistsException(String resourcevalue) {
		super( "Username or Email already exist with value " + resourcevalue);
	}

}
