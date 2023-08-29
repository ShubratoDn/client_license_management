package com.license.manager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role {

	@Id	
	private Long id;
	private String role;
	
}
