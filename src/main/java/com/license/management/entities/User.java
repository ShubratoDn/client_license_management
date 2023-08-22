package com.license.management.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private String username;
	private String password;
	private String email;
	private String fullName;
	private Timestamp dateCreated;
	private Timestamp dateUpdated;
	private Timestamp dateLastLogin;
	private boolean isLocked;
	
	@ManyToMany( fetch = FetchType.EAGER)
	private List<Role> roles;
	
}