package com.license.manager.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.license.manager.DTO.LicenseDTO;
import com.license.manager.DTO.TransactionDTO;
import com.license.manager.DTO.UserDTO;
import com.license.manager.DTO.UserLicenseDTO;
import com.license.manager.entities.License;
import com.license.manager.entities.User;
import com.license.manager.entities.UserLicense;
import com.license.manager.exceptions.TransactionFailedException;
import com.license.manager.repositories.UserLicenseRepository;
import com.license.manager.services.TransactionService;
import com.license.manager.services.UserLicenseService;


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
