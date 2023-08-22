package com.license.management.DTO;

import java.sql.Timestamp;
import java.util.List;

import com.license.management.entities.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

	private Long userId;
	private String username;
	private String password;
	private String email;
	private String fullName;
	private Timestamp dateCreated;
	private Timestamp dateUpdated;
	private Timestamp dateLastLogin;
	private boolean isLocked;
	
	@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<Role> roles;	
}
