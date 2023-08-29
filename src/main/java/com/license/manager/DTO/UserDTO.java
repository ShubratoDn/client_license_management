package com.license.manager.DTO;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.license.manager.entities.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

	private Long userId;
	@Size(min = 3, message = "Username should contain minimum 3 character")
	private String username;
	private String password;
	@Email(message = "Insert a valid email")
	private String email;
	private String fullName;
	private Timestamp dateCreated;
	private Timestamp dateUpdated;
	private Timestamp dateLastLogin;
	private boolean isLocked;
	
	
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}



	@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<Role> roles;	
}
