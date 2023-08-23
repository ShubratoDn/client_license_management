package com.license.management.controllers;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.license.management.DTO.UserDTO;
import com.license.management.payloads.ErrorResponse;
import com.license.management.services.UserServices;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private UserServices userServices;
	
	@DeleteMapping("/delete/user/{userId}")
	public ResponseEntity<?> test(@PathVariable Long userId){
		
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
				
		return ResponseEntity.ok("okay");
	}
	
}