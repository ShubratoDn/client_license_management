package com.license.manager.services;

import java.util.List;

import com.license.manager.DTO.LicenseDTO;

public interface LicenseServices {

	public LicenseDTO addLicense(LicenseDTO licenseDTO);
	
	public LicenseDTO matchLicese(LicenseDTO licenseDTO);
	
	public LicenseDTO getLicenseById(Long licenseId);
	
	public List<LicenseDTO> getAllLicenses();
	
	public void deleteLicense(Long licenseId);
	
	public LicenseDTO updateLicense(LicenseDTO licenseDTO);
}
