package com.license.manager.servicesImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.license.manager.DTO.UserDTO;
import com.license.manager.entities.Role;
import com.license.manager.entities.User;
import com.license.manager.exceptions.ResourceNotFoundException;
import com.license.manager.exceptions.UsernameAlreadyExistsException;
import com.license.manager.payloads.PageableResponse;
import com.license.manager.repositories.RoleRepository;
import com.license.manager.repositories.UserRepository;
import com.license.manager.services.UserServices;

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
	
	public UserDTO getUserByEmail(String email) {
		User findByEmail = userRepository.findByEmail(email);
		if(findByEmail == null) {
			throw new ResourceNotFoundException("User", email);
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

	@Override
	public UserDTO updateProfile(UserDTO userDto) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsernameOrEmail(name, name);
		
		
		//if the email doesn't match with existing email
		if(userDto.getEmail() != null && !userDto.getEmail().isBlank() && !userDto.getEmail().equals(user.getEmail())) {
			User findByEmail = userRepository.findByEmail(userDto.getEmail());
			if(findByEmail != null) {
				throw new UsernameAlreadyExistsException(name);
			}
			
			user.setEmail(userDto.getEmail());
		}
		
		
		if(userDto.getUsername() != null && !userDto.getUsername().isBlank() && !userDto.getUsername().equals(user.getUsername())) {
			User findByUsername= userRepository.findByUsername(userDto.getUsername());
			if(findByUsername != null) {
				throw new UsernameAlreadyExistsException(userDto.getUsername());
			}
			
			user.setUsername(userDto.getUsername());
		}
		
		
		
		if(userDto.getFullName() != null && !userDto.getFullName().isBlank() && !userDto.getFullName().equals(user.getFullName())) {
			user.setFullName(userDto.getFullName());
		}
		
//		System.out.println(userDto.getPassword()+"NEW PASS");
//		if(userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
//			String newPass= bCryptPasswordEncoder.encode(userDto.getPassword());
//			String oldPass = user.getPassword();
//			
//			if(!bCryptPasswordEncoder.matches(newPass, oldPass)) {
//				System.out.println("password Changed");
//				System.out.println("new pass " + newPass);
//				System.out.println("old pass " + oldPass);
//				user.setPassword(newPass);
//			}else {
//				System.out.println("password NOT Changed");
//			}
//		}
		
		user.setDateUpdated(Timestamp.from(Instant.now()));
		
		User updatedUser = userRepository.save(user);
		
		UserDTO map = modelMapper.map(updatedUser, UserDTO.class);		
		return map;
	}

	
	
	
	@Override
	public PageableResponse getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection) {
		Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		// STEP pagable
		Page<User> pageInfo;
		try {
			Pageable page = PageRequest.of(pageNumber, pageSize, sort);
			pageInfo = userRepository.findAll(page);
		} catch (Exception e) {
			// jodi sort by er value incorrect hoy tahole eta cholbe
			Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("userId").descending());
			pageInfo = userRepository.findAll(page);
		}
		
		List<User> content = pageInfo.getContent();		
	    List<UserDTO> userDTOs = content.stream()
	            .map(user -> modelMapper.map(user, UserDTO.class))
	            .collect(Collectors.toList());
	    
	    
	    PageableResponse pageData = new PageableResponse();
		pageData.setContent(userDTOs);
		pageData.setPageNumber(pageInfo.getNumber());
		pageData.setPageSize(pageInfo.getSize());
		pageData.setTotalElements(pageInfo.getTotalElements());
		pageData.setTotalPages(pageInfo.getTotalPages());
		pageData.setNumberOfElements(pageInfo.getNumberOfElements());

		pageData.setEmpty(pageInfo.isEmpty());
		pageData.setFirst(pageInfo.isFirst());
		pageData.setLast(pageInfo.isLast());
		
		return pageData;
	}
	
}
