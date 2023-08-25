package com.license.management.DTO;

import com.license.management.entities.License;
import com.license.management.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLicenseDTO {
	private Long userLicenseId;
	
	private License license;

    private User user;
	
	private boolean isActive;
}
