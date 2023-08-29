package com.license.manager.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.license.manager.DTO.UserDTO;
import com.license.manager.payloads.ErrorResponse;
import com.license.manager.payloads.PageableResponse;
import com.license.manager.services.UserServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {


	
	@Autowired
	private UserServices userServices;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId){
		
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(userId);
		
		UserDTO userById = userServices.getUserById(userDTO);
		if (userById == null) {
		    // User with the given ID is not found
		    ErrorResponse errorResponse = new ErrorResponse(
		        LocalDateTime.now(),
		        HttpStatus.NOT_FOUND.value(),
		        "Not Found",
		        "User with the given ID is not found."
		    );
		    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}


		userServices.deleteUser(userDTO);
				
		return ResponseEntity.ok("Deleted User Successfully");
	}
	
	
	
	
	
	
	
	@PutMapping("/user")
	public ResponseEntity<?> updateProfile(@Valid @RequestBody UserDTO userDTO){
		UserDTO updateProfile = userServices.updateProfile(userDTO);
		return ResponseEntity.ok(updateProfile);
	}
	
	
	
	/**
	 * Retrieves a user by their unique identifier.
	 *
	 * @param userId The ID of the user to retrieve.
	 * @return A ResponseEntity containing the retrieved UserDTO if found, or an ErrorResponse if the user is not found.
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId){
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(userId);
		
		UserDTO userById = userServices.getUserById(userDTO);
		if (userById == null) {
		    // User with the given ID is not found
		    ErrorResponse errorResponse = new ErrorResponse(
		        LocalDateTime.now(),
		        HttpStatus.NOT_FOUND.value(),
		        "Not Found",
		        "User with the given ID is not found."
		    );
		    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		
		return ResponseEntity.ok(userById);
	}
	
	
	
	
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getAllUsers(
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size,
			@RequestParam(value = "sortBy", defaultValue = "userId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection
			){
		
		if(size == 0) {
			size= 5;
		}
		
		
		PageableResponse allUsers = userServices.getAllUsers(page, size, sortBy, sortDirection);
		
		return ResponseEntity.ok(allUsers);
	}
	
	
	
	
	
	
}
