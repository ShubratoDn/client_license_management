package com.license.management.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.license.management.DTO.UserDTO;
import com.license.management.payloads.RegisterRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	@Autowired
	private ModelMapper mapper;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(){
		return ResponseEntity.ok("ok");
	}
	
	
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
		System.out.println("WORKING....");		
		
		UserDTO userDTO = mapper.map(registerRequest, UserDTO.class);
		System.out.println(userDTO);
		
		
		return ResponseEntity.ok(userDTO);
	}
	
}
