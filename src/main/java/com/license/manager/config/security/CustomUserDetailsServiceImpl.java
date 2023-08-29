package com.license.manager.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.license.manager.entities.User;
import com.license.manager.repositories.UserRepository;

/**
 * Custom implementation of the Spring Security UserDetailsService interface.
 * This class is used to load user details by their username (in this case, email) during authentication.
 * It fetches user information from the UserRepository and converts it into a UserDetails object
 * that Spring Security can use for authentication and authorization purposes.
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepo.findByUsernameOrEmail(username, username);
		if(user == null) {
			throw new UsernameNotFoundException("User name not found");
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);		
		return customUserDetails;
	}

}
