package com.license.management.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.license.management.DTO.UserDTO;
import com.license.management.config.Constants;
import com.license.management.entities.Role;
import com.license.management.payloads.ErrorResponse;
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
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(){
		return ResponseEntity.ok("ok");
	}
	
	
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
	
}
