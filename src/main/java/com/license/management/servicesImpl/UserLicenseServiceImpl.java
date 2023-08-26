package com.license.management.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.license.management.DTO.LicenseDTO;
import com.license.management.DTO.ProductDTO;
import com.license.management.DTO.TransactionDTO;
import com.license.management.DTO.UserDTO;
import com.license.management.DTO.UserLicenseDTO;
import com.license.management.entities.License;
import com.license.management.entities.User;
import com.license.management.entities.UserLicense;
import com.license.management.exceptions.TransactionFailedException;
import com.license.management.repositories.UserLicenseRepository;
import com.license.management.services.TransactionService;
import com.license.management.services.UserLicenseService;


@Service
public class UserLicenseServiceImpl implements UserLicenseService {
		
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UserLicenseRepository userLicenseRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserLicenseDTO purchaseLicense(UserDTO userDTO, LicenseDTO licenseDTO) {		
		TransactionDTO addTransaction = transactionService.addTransaction(userDTO, licenseDTO);
		if(addTransaction == null) {
			throw new TransactionFailedException(licenseDTO.getLicenseId().toString());
		}
		
		UserLicense userLicense = new UserLicense();
		userLicense.setActive(true);
		userLicense.setLicense(modelMapper.map(licenseDTO, License.class));
		userLicense.setUser(modelMapper.map(userDTO, User.class));
		
		UserLicense save = userLicenseRepository.save(userLicense);
				
		return modelMapper.map(save, UserLicenseDTO.class);
	}
	
	
	
	
	public UserLicenseDTO getByUserAndLicense(UserDTO userDTO, LicenseDTO licenseDTO) {		
		License license = modelMapper.map(licenseDTO, License.class);
		User user = modelMapper.map(userDTO, User.class);
		
		UserLicense findByUserAndLicense = userLicenseRepository.findByUserAndLicense(user, license);
		
		if(findByUserAndLicense == null) {
			return null;
		}
		
		return modelMapper.map(findByUserAndLicense, UserLicenseDTO.class);
	}
	
	
	
	
	public List<UserLicenseDTO> getUserLicenses(UserDTO userDTO) {
		List<UserLicense> licences = userLicenseRepository.findByUserOrderByUserLicenseIdDesc(modelMapper.map(userDTO, User.class));
		
	    // Use Java Streams to map the products to ProductDTO
	    List<UserLicenseDTO> licenseDTOs = licences.stream()
	            .map(licence -> modelMapper.map(licence, UserLicenseDTO.class))
	            .collect(Collectors.toList());

		return licenseDTOs;
	}
	
	
	

}
