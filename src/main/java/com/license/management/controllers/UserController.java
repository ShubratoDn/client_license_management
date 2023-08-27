package com.license.management.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.license.management.DTO.UserDTO;
import com.license.management.payloads.ErrorResponse;
import com.license.management.services.UserServices;

@RestController
@RequestMapping("/api/v1")
public class UserController {


	
	@Autowired
	private UserServices userServices;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/user/{userId}")
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
	
	
	
	
	
	
	
	@PostMapping("/update-profile")
	public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO){		
		UserDTO updateProfile = userServices.updateProfile(userDTO);
		return ResponseEntity.ok(updateProfile);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
