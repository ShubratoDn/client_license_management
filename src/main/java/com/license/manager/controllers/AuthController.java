package com.license.manager.controllers;

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

import com.license.manager.DTO.UserDTO;
import com.license.manager.config.Constants;
import com.license.manager.config.jwt.JwtTokenUtil;
import com.license.manager.config.security.CustomUserDetailsServiceImpl;
import com.license.manager.entities.Role;
import com.license.manager.payloads.ErrorResponse;
import com.license.manager.payloads.LoginRequest;
import com.license.manager.payloads.LoginResponse;
import com.license.manager.payloads.RegisterRequest;
import com.license.manager.services.UserServices;

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
	@PostMapping("/user")
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
                "User with the same email or username already registered."
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        // Determine user role based on secret code
		if(registerRequest.getSecretCode() != null && registerRequest.getSecretCode().equals(Constants.ADMIN_SECRET)) {
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Logs in a user and generates an authentication token.
	 *
	 * @param loginRequest The login request containing the username and password.
	 * @return ResponseEntity containing the authentication token and user information if login is successful,
	 *         or an error response if login fails.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
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
	        log.error("Login failed for user '{}': User account is disabled.", username);
	        return ResponseEntity.badRequest().body(errorResponse);
	    } catch (BadCredentialsException e) {
	        ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.UNAUTHORIZED.value(),
	            "Bad Credentials",
	            "Incorrect username or password."
	        );
	        log.error("Login failed for user '{}': Incorrect username or password.", username);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	    } catch (InternalAuthenticationServiceException e) {
	        ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "User Not Found",
	            "User not found."
	        );
	        log.error("Login failed for user '{}': User not found.", username);
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

	    userServices.updateLastLogin(user);
	    
	    log.info("User '{}' has successfully logged in.", username);
	    return ResponseEntity.ok(loginResponse);
	}

	
	
	
	
}
