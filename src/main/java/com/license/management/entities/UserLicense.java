package com.license.management.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserLicense {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long userLicenseId;
	private License license;
	private User user;
	
}
