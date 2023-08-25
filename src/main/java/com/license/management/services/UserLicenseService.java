package com.license.management.services;

import java.util.List;

import com.license.management.DTO.LicenseDTO;
import com.license.management.DTO.UserDTO;
import com.license.management.DTO.UserLicenseDTO;

public interface UserLicenseService {
	public UserLicenseDTO purchaseLicense(UserDTO userDTO, LicenseDTO licenseDTO);
	
	public UserLicenseDTO getByUserAndLicense(UserDTO userDTO, LicenseDTO licenseDTO);
	
	public List<UserLicenseDTO> getUserLicenses(UserDTO userDTO);
}
