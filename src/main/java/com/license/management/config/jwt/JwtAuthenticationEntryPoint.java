package com.license.management.config.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.license.management.payloads.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		// Create an ErrorResponse object
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Replace with actual timestamp logic
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                authException.getMessage()
        );
		
		// Serialize the ErrorResponse object to JSON
		ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.registerModule(new JavaTimeModule());
		String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

		// Write the JSON response to the output stream
		try (PrintWriter out = response.getWriter()) {
			out.print(errorResponseJson);
		}
		
	}

}
