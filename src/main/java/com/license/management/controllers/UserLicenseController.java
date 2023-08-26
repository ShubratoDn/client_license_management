package com.license.management.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.license.management.DTO.LicenseDTO;
import com.license.management.DTO.TransactionHistoryDto;
import com.license.management.DTO.UserDTO;
import com.license.management.DTO.UserLicenseDTO;
import com.license.management.payloads.ErrorResponse;
import com.license.management.payloads.PurchasedLicenseBody;
import com.license.management.services.LicenseServices;
import com.license.management.services.TransactionService;
import com.license.management.services.UserLicenseService;
import com.license.management.services.UserServices;

@RestController
@RequestMapping("/api/v1")
public class UserLicenseController {

	@Autowired
	private UserServices userServices;
	
	@Autowired
	private LicenseServices licenseServices;
	
	@Autowired
	private UserLicenseService userLicenseService;
	
	@Autowired
	private TransactionService transactionService;	
	
	@Autowired
	private ModelMapper  modelMapper;
	
	@PostMapping("/purchase-license/{licenseId}")
	public ResponseEntity<?> purchaseLicense(@PathVariable Long licenseId){		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		UserDTO user = userServices.getUserByEmail(username);
		LicenseDTO license = licenseServices.getLicenseById(licenseId);
		
		if (userLicenseService.getByUserAndLicense(user, license) != null) {
		    ErrorResponse errorResponse = new ErrorResponse(
		        LocalDateTime.now(),
		        HttpStatus.BAD_REQUEST.value(),
		        "License Already Purchased",
		        "You have already purchased this license."
		    );
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		UserLicenseDTO purchaseLicense = userLicenseService.purchaseLicense(user, license);		

		return ResponseEntity.ok(purchaseLicense);
	}
	
	
	
	@GetMapping("/my-licenses")
	public ResponseEntity<?> myLicences(){		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		UserDTO user = userServices.getUserByEmail(username);		
		List<UserLicenseDTO> userLicenses = userLicenseService.getUserLicenses(user);		
		List<PurchasedLicenseBody> purchasedLicenseBodies = new ArrayList<>();
		
		for(UserLicenseDTO userLicenseDTO: userLicenses) {
			PurchasedLicenseBody map = modelMapper.map(userLicenseDTO, PurchasedLicenseBody.class);
			map.setUsername(username);
			purchasedLicenseBodies.add(map);
		}
		
		return ResponseEntity.ok(purchasedLicenseBodies);
	}
	
	
	
	@GetMapping("/my-transactions")
	public ResponseEntity<?> myTransactions(){		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		UserDTO user = userServices.getUserByEmail(username);
		
		List<TransactionHistoryDto> myAllTransactions = transactionService.getMyAllTransactions(user);
		
		return ResponseEntity.ok(myAllTransactions);
	}
	
	
}
