package com.license.management.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.license.management.payloads.ErrorResponse;

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

	
}
