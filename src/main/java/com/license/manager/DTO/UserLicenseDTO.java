package com.license.manager.DTO;

import com.license.manager.entities.License;
import com.license.manager.entities.User;

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
