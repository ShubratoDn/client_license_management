package com.license.manager.config.jwt;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.license.manager.config.security.CustomUserDetailsServiceImpl;
import com.license.manager.payloads.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	
	/**
     * Filters incoming requests to validate JWT tokens and set the user's authentication status.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If a servlet exception occurs.
     * @throws IOException      If an I/O exception occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (header != null && header.startsWith("Bearer")) {
            token = header.substring(7);

            try {
                username = jwtTokenUtil.extractUsername(token);
                log.info("User Found: " + username);

            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT token (Filter Class)", e);
            } catch (ExpiredJwtException e) {                
                // Create an ErrorResponse object
	            ErrorResponse errorResponse = new ErrorResponse(
	                    LocalDateTime.now(), 
	                    HttpStatus.UNAUTHORIZED.value(),
	                    "Session has expired",
	                    e.getMessage()
	            );
	            
	            // Serialize the ErrorResponse object to JSON
	            ObjectMapper objectMapper = new ObjectMapper();
            	objectMapper.registerModule(new JavaTimeModule());
	            String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

	            // Set the HTTP status code and response content type
	            response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

	            // Write the JSON error response to the response body
	            response.getWriter().write(errorResponseJson);
	            return;                
                
            } catch (MalformedJwtException e) {
                log.error("Invalid JWT (from Filter Class)", e);
            } catch (SignatureException e) { 	            
                // JWT signature verification failed
                // Return an appropriate HTTP response to the client
            	 ErrorResponse x = new ErrorResponse(
 	                    LocalDateTime.now(), 
 	                    HttpStatus.UNAUTHORIZED.value(),
 	                    "Malformed Token",
 	                    e.getMessage()
 	            );
            	 
            	ObjectMapper objectMapper = new ObjectMapper();
            	objectMapper.registerModule(new JavaTimeModule());
 	            String errorResponseJson = objectMapper.writeValueAsString(x);
            	 
 	            
 	            // Set the HTTP status code and response content type
	            response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

	            // Write the JSON error response to the response body
	            response.getWriter().write(errorResponseJson);
	            response.getWriter().flush();
	            return; 
            	}
            }        
        
        

        // VALIDATING THE TOKEN
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                log.error("Failed validating the token");
            }
        } else {
            log.error("Username not found from the TOKEN");
        }

        filterChain.doFilter(request, response);
    }	
	
}
