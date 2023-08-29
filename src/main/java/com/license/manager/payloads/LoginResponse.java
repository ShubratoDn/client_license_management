package com.license.manager.payloads;


import com.license.manager.DTO.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

	private String token;
	private UserDTO user;
	
}
