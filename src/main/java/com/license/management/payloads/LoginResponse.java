package com.license.management.payloads;


import com.license.management.DTO.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

	private UserDTO user;
	private String token;
	
}
