package com.license.manager.services;

import com.license.manager.DTO.UserDTO;
import com.license.manager.payloads.PageableResponse;

public interface UserServices {

	public UserDTO saveUser(UserDTO userDto);
	
	public UserDTO getUserById(UserDTO userDTO);
	
	public UserDTO getUserByEmail(UserDTO userDTO);
	
	public UserDTO getUserByEmail(String email);
	
	public UserDTO getUserByEmailOrUsername(UserDTO userDTO);
	
	public UserDTO deleteUser(UserDTO dto);
	
	public void updateLastLogin(UserDTO user);
	
	public UserDTO updateProfile(UserDTO userDto);
	
	public PageableResponse getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection);
	
}
