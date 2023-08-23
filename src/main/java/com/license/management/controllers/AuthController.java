package com.license.management.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.license.management.DTO.UserDTO;
import com.license.management.config.Constants;
import com.license.management.config.jwt.JwtTokenUtil;
import com.license.management.config.security.CustomUserDetailsServiceImpl;
import com.license.management.entities.Role;
import com.license.management.payloads.ErrorResponse;
import com.license.management.payloads.LoginRequest;
import com.license.management.payloads.LoginResponse;
import com.license.management.payloads.RegisterRequest;
import com.license.management.services.UserServices;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil; 
	
	
	
	/**
     * Registers a new user.
     *
     * @param registerRequest The registration request containing user information.
     * @return ResponseEntity containing the saved user's DTO.
     */
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){	
		 log.info("Registering new user: {}", registerRequest.getUsername());
		
		 // Map RegisterRequest to UserDTO
        UserDTO userDTO = mapper.map(registerRequest, UserDTO.class);

        if(userServices.getUserByEmailOrUsername(userDTO) != null) {
        	 // User with the same email already exists
            ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "User with the same email or Username already registered."
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        // Determine user role based on secret code
		if(registerRequest.getSecretCode().equals(Constants.ADMIN_SECRET)) {
			Role adminRole = new Role();
			adminRole.setId(Constants.ROLE_ADMIN);
			userDTO.setRoles(List.of(adminRole));
		}else {
			Role userRole = new Role();
			userRole.setId(Constants.ROLE_USER);
			userDTO.setRoles(List.of(userRole));
		}
		
		// Save the user
        UserDTO savedUser = userServices.saveUser(userDTO);

        log.info("New user registered successfully: {}", savedUser.getUsername());

        return ResponseEntity.ok(savedUser);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		
		
		try {
		    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
		    ErrorResponse errorResponse = new ErrorResponse(
		        LocalDateTime.now(),
		        HttpStatus.BAD_REQUEST.value(),
		        "User Account Disabled",
		        "User account is disabled."
		    );
		    return ResponseEntity.badRequest().body(errorResponse);
		} catch (BadCredentialsException e) {
		    ErrorResponse errorResponse = new ErrorResponse(
		        LocalDateTime.now(),
		        HttpStatus.UNAUTHORIZED.value(),
		        "Bad Credentials",
		        "Incorrect username or password."
		    );
		    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		} catch (InternalAuthenticationServiceException e) {
		    ErrorResponse errorResponse = new ErrorResponse(
		        LocalDateTime.now(),
		        HttpStatus.BAD_REQUEST.value(),
		        "User Not Found",
		        "User not found."
		    );
		    return ResponseEntity.badRequest().body(errorResponse);
		}
		UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
		String token = jwtTokenUtil.generateToken(userDetails);
		
		
		
		UserDTO userDto = new UserDTO();
		userDto.setEmail(username);
		userDto.setUsername(username);
		
		UserDTO user = userServices.getUserByEmailOrUsername(userDto);
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		loginResponse.setUser(user);
		
		return ResponseEntity.ok(loginResponse);
		
	}
	
}
