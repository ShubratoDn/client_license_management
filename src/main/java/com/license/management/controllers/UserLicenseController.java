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
	
	/**
	 * Purchase a license for the currently authenticated user.
	 *
	 * @param licenseId The ID of the license to purchase.
	 * @return A ResponseEntity containing a UserLicenseDTO representing the purchased license.
	 */
	@PostMapping("/purchase-license/{licenseId}")
	public ResponseEntity<?> purchaseLicense(@PathVariable Long licenseId) {
	    // Get the username of the currently authenticated user
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // Retrieve user information by email
	    UserDTO user = userServices.getUserByEmail(username);

	    // Retrieve the license to purchase by its ID
	    LicenseDTO license = licenseServices.getLicenseById(licenseId);

	    // Check if the user has already purchased this license
	    if (userLicenseService.getByUserAndLicense(user, license) != null) {
	        ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "License Already Purchased",
	            "You have already purchased this license."
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	    }

	    // Purchase the license for the user
	    UserLicenseDTO purchasedLicense = userLicenseService.purchaseLicense(user, license);

	    // Return the purchased license as a ResponseEntity
	    return ResponseEntity.ok(purchasedLicense);
	}

	
	
	
	/**
	 * Get licenses purchased by the currently authenticated user.
	 *
	 * @return A ResponseEntity containing a list of PurchasedLicenseBody representing the user's licenses.
	 */
	@GetMapping("/my-licenses")
	public ResponseEntity<?> myLicences() {
	    // Get the username of the currently authenticated user
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // Retrieve user information by email
	    UserDTO user = userServices.getUserByEmail(username);

	    // Retrieve licenses purchased by the user
	    List<UserLicenseDTO> userLicenses = userLicenseService.getUserLicenses(user);

	    // Create a list to hold the response data
	    List<PurchasedLicenseBody> purchasedLicenseBodies = new ArrayList<>();

	    // Map and populate the response data
	    for (UserLicenseDTO userLicenseDTO : userLicenses) {
	        PurchasedLicenseBody map = modelMapper.map(userLicenseDTO, PurchasedLicenseBody.class);
	        map.setUsername(username);
	        purchasedLicenseBodies.add(map);
	    }

	    // Return the list of user's purchased licenses as a ResponseEntity
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
