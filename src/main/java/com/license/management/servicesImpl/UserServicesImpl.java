package com.license.management.servicesImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.license.management.DTO.UserDTO;
import com.license.management.entities.Role;
import com.license.management.entities.User;
import com.license.management.repositories.RoleRepository;
import com.license.management.repositories.UserRepository;
import com.license.management.services.UserServices;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServicesImpl implements UserServices {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	/**
     * Saves a user.
     *
     * @param userDto The UserDTO containing user information to be saved.
     * @return The saved UserDTO.
     */
	@Override
	public UserDTO saveUser(UserDTO userDto) {
        log.info("Saving user: {}", userDto.getUsername());

        // Map UserDTO to User entity
        User user = modelMapper.map(userDto, User.class);

        // Set additional user properties
        Timestamp dateCreated = Timestamp.from(Instant.now());
        user.setDateCreated(dateCreated);
        user.setDateLastLogin(null);
        user.setDateUpdated(null);
        user.setLocked(false);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // Save the user
        User savedUser = userRepository.save(user);

        // Map the saved user back to UserDTO
        UserDTO savedUserDto = modelMapper.map(savedUser, UserDTO.class);

        // Retrieve and set user roles
        List<Role> userRoles = new ArrayList<>();
        for (Role role : savedUser.getRoles()) {
            Role userRole = roleRepository.findById(role.getId()).orElse(null);
            if (userRole != null) {
                userRoles.add(userRole);
            }
        }
        savedUserDto.setRoles(userRoles);

        log.info("User saved successfully: {}", savedUserDto.getUsername());

        return savedUserDto;
    }

	@Override
	public UserDTO getUserByEmail(UserDTO userDTO) {
		User findByEmail = userRepository.findByEmail(userDTO.getEmail());
		if(findByEmail == null) {
			return null;
		}
		
		UserDTO map = modelMapper.map(findByEmail, UserDTO.class);		
		return map;
	}

	@Override
	public UserDTO getUserByEmailOrUsername(UserDTO userDTO) {
		User findByUsernameOrEmail = userRepository.findByUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail());
		
		if(findByUsernameOrEmail == null) {
			return null;
		}
		
		UserDTO map = modelMapper.map(findByUsernameOrEmail, UserDTO.class);
		
		return map;
	}

	@Override
	public UserDTO deleteUser(UserDTO dto) {
		User map = modelMapper.map(dto, User.class);		
		userRepository.delete(map);		
		return null;
	}
	
	

	public UserDTO getUserById(UserDTO userDTO) {
		User user = userRepository.findById(userDTO.getUserId()).orElse(null);
		if(user == null) {
			return null;
		}
		UserDTO map = modelMapper.map(user, UserDTO.class);		
		return map;
	}

	
	
	
	@Override
	public void updateLastLogin(UserDTO userDto ) {
		User user = userRepository.findById(userDto.getUserId()).orElse(null);
		user.setDateLastLogin(Timestamp.from(Instant.now()));
		
		userRepository.save(user);
		
	}
	
	
}
