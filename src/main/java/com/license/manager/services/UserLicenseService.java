package com.license.manager.services;

import java.util.List;

import com.license.manager.DTO.LicenseDTO;
import com.license.manager.DTO.UserDTO;
import com.license.manager.DTO.UserLicenseDTO;

public interface UserLicenseService {
	public UserLicenseDTO purchaseLicense(UserDTO userDTO, LicenseDTO licenseDTO);
	
	public UserLicenseDTO getByUserAndLicense(UserDTO userDTO, LicenseDTO licenseDTO);
	
	public List<UserLicenseDTO> getUserLicenses(UserDTO userDTO);
}
