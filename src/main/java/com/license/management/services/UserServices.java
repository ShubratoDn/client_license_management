package com.license.management.services;

import com.license.management.DTO.UserDTO;

public interface UserServices {

	public UserDTO saveUser(UserDTO userDto);
	
	public UserDTO getUserById(UserDTO userDTO);
	
	public UserDTO getUserByEmail(UserDTO userDTO);
	
	public UserDTO getUserByEmailOrUsername(UserDTO userDTO);
	
	public UserDTO deleteUser(UserDTO dto);
	
	public void updateLastLogin(UserDTO user );
}
