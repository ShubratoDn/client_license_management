package com.license.management.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String resourceName, String resouceValue) {
		super(resourceName + " not found for value "+ resouceValue);
	}
}
