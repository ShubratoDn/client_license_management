package com.license.management.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.license.management.payloads.ErrorResponse;

import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
     * Handles the HttpMessageNotReadableException and returns an error response.
     *
     * @param ex The HttpMessageNotReadableException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Error occurred while processing the request.", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Replace with actual timestamp logic
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Request body is not readable or is in an invalid format."
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    
    
    /**
     * Handles the HttpMediaTypeNotSupportedException and returns an error response.
     *
     * @param ex The HttpMediaTypeNotSupportedException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("Error occurred while processing the request.", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Replace with actual timestamp logic
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "Unsupported Media Type",
                "Unsupported media type in the request."
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    
    
    
    /**
     * Handles the HttpRequestMethodNotSupportedException and returns an error response.
     *
     * @param ex The HttpRequestMethodNotSupportedException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("Error occurred while processing the request.", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Replace with actual timestamp logic
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                ""+ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    
    
    
    
  //validation error handler
  	@ExceptionHandler(MethodArgumentNotValidException.class)
  	public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler (MethodArgumentNotValidException ex){
  		
  		Map<String, String> response = new HashMap<>(); 
  		response.put("error", "Invalid Input");
  		
  		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
  		
  		for(ObjectError error : allErrors) {
  			String fieldName = ((FieldError) error).getField();
  			String message = error.getDefaultMessage();			
  			response.put(fieldName, message);
  		}		
  		return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);		
  	}
  	

  	
  	/**
  	 * Handles the UsernameNotFoundException and returns an error response.
  	 *
  	 * @param ex The UsernameNotFoundException to handle.
  	 * @return A ResponseEntity containing the error response.
  	 */
  	@ExceptionHandler(UsernameNotFoundException.class)
  	public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
  	    log.error("Error occurred while processing the request.", ex);

  	    ErrorResponse errorResponse = new ErrorResponse(
  	            LocalDateTime.now(), // Replace with actual timestamp logic
  	            HttpStatus.NOT_FOUND.value(),
  	            "User Not Found",
  	            "The requested user was not found."
  	    );

  	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  	}
  	
  	
  	
  	/**
  	 * Exception handler for handling SignatureException.
  	 *
  	 * @param ex The SignatureException to handle.
  	 * @return A ResponseEntity containing the error response for unauthorized requests.
  	 */
  	@ExceptionHandler(SignatureException.class)
  	public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex) {
  	    ErrorResponse errorResponse = new ErrorResponse(
  	            LocalDateTime.now(),
  	            HttpStatus.UNAUTHORIZED.value(),
  	            "Unauthorized",
  	            "Signature verification failed: " + ex.getMessage()
  	    );

  	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  	}


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Access Denied",
            "You do not have permission to access this resource."
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
  	
	
}
