package com.license.manager.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.license.manager.DTO.LicenseDTO;
import com.license.manager.DTO.TransactionDTO;
import com.license.manager.DTO.TransactionHistoryDto;
import com.license.manager.DTO.UserDTO;
import com.license.manager.DTO.UserLicenseDTO;
import com.license.manager.payloads.ErrorResponse;
import com.license.manager.payloads.PageableResponse;
import com.license.manager.payloads.PurchasedLicenseBody;
import com.license.manager.services.LicenseServices;
import com.license.manager.services.TransactionService;
import com.license.manager.services.UserLicenseService;
import com.license.manager.services.UserServices;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TransactionController {

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
	
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/transaction")
	public ResponseEntity<?> getAllTransaction(			
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size,
			@RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection
			){
		PageableResponse allTransationPageable = transactionService.getAllTransationPageable(page, size, sortBy, sortDirection);
		return ResponseEntity.ok(allTransationPageable);
	}
	
	
	
	//get transaction by id
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/transaction/{transactionId}")
	public ResponseEntity<?> getTransactionById(@PathVariable long transactionId){		
		TransactionDTO transactionById = transactionService.getTransactionById(transactionId);
		return ResponseEntity.ok(transactionById);
	}
	
	

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/transaction/{transactionId}")
	public ResponseEntity<?> deleteTransactionById(@PathVariable long transactionId){	
	    log.info("Transaction with ID: {}", transactionId);

	    transactionService.getTransactionById(transactionId);
	    
	    transactionService.deleteTransactionById(transactionId);

	    log.info("Transaction with ID {} has been deleted.", transactionId);
	    return ResponseEntity.ok("Transaction with ID " + transactionId + " has been deleted.");
	}
	
	
	
	
	//update Transaction
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/transaction/{transactionId}")
	public ResponseEntity<?> updateTransactionById(@PathVariable long transactionId, @RequestBody TransactionDTO transactionDTO){	
	    log.info("Transaction with ID: {}", transactionId);
	    
	    transactionService.getTransactionById(transactionId);
	    
	    TransactionDTO updateTransactionById = transactionService.updateTransactionById(transactionId, transactionDTO);
	    
	    log.info("Transaction with ID {} has been updated.", transactionId);
	    return ResponseEntity.ok(updateTransactionById);
	}
	
	
	
	
	
	
	
	
}
