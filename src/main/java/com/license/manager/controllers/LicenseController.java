package com.license.manager.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.license.manager.DTO.LicenseDTO;
import com.license.manager.DTO.ProductDTO;
import com.license.manager.entities.License;
import com.license.manager.exceptions.InvalidDateFormatException;
import com.license.manager.payloads.ErrorResponse;
import com.license.manager.repositories.LicenseRepository;
import com.license.manager.services.LicenseServices;
import com.license.manager.services.ProductServices;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class LicenseController {
	
	@Autowired
	private LicenseServices licenseServices;
	
	@Autowired
	private ProductServices productServices;
	
	
	
	 /**
     * Adds a new license.
     *
     * @param licenseDTO The license data to be added.
     * @return A ResponseEntity with the added license or an error response.
     */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add-license")
	public ResponseEntity<?> addLicense(@Valid @RequestBody LicenseDTO licenseDTO){
		log.info("Received a request to add a new license.");
		LicenseDTO matchLicense = licenseServices.matchLicese(licenseDTO);		
		if (matchLicense != null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
					"License Already Exists", "A license for the specified product already exists.");
		    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
		}

		LicenseDTO addLicense = licenseServices.addLicense(licenseDTO);
		log.info("New license added successfully.");
		return ResponseEntity.ok(addLicense);
	}
	
	
	
	
	//delete license	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/license/{licenseId}")
	public ResponseEntity<?> deleteLicense(@PathVariable long licenseId){
		LicenseDTO licenseById = licenseServices.getLicenseById(licenseId);
		licenseServices.deleteLicense(licenseById.getLicenseId());
		return ResponseEntity.ok("License id: "+licenseId+" has been deleted successfully");
	}
	
	
	
	
	/**
     * Updates an existing license based on the provided LicenseDTO.
     *
     * @param licenseDTO The LicenseDTO containing the updated license information.
     * @return A ResponseEntity with the updated license or an error response.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update-license")
    public ResponseEntity<?> updateLicense(@RequestBody LicenseDTO licenseDTO) {
        log.info("Received a request to update a license.");

        // Retrieve the existing license by its ID
        LicenseDTO license = licenseServices.getLicenseById(licenseDTO.getLicenseId());

        // Validate and update product if specified in the request
        if (licenseDTO.getProduct() != null && licenseDTO.getProduct().getProductId() != null &&
            !licenseDTO.getProduct().getProductId().equals(license.getProduct().getProductId())) {
            ProductDTO productById = productServices.getProductById(licenseDTO.getProduct().getProductId());
            license.setProduct(productById);
            log.info("Updated license product.");
        }

        // Update the expiration date if specified in the request
        if (licenseDTO.getExpiringDate() != null && !licenseDTO.getExpiringDate().equals(license.getExpiringDate())) {
            license.setExpiringDate(licenseDTO.getExpiringDate());
            log.info("Updated license expiration date.");
        }

        // Update the state if specified in the request
        if (licenseDTO.getState() != null) {
            license.setState(licenseDTO.getState());
            log.info("Updated license state.");
        }

        // Update the price if specified in the request and it's a positive value
        if (licenseDTO.getPrice() > 0) {
            license.setPrice(licenseDTO.getPrice());
            log.info("Updated license price.");
        }

        // Update the license in the service
        LicenseDTO updateLicense = licenseServices.updateLicense(license);

        log.info("License updated successfully.");
        return ResponseEntity.ok(updateLicense);
    }
	
    
    
    
    
    
    @GetMapping("/license/{licenseId}")
    public ResponseEntity<?> getLicenseById(@PathVariable Long licenseId){    	
    	LicenseDTO licenseById = licenseServices.getLicenseById(licenseId);    	
    	return ResponseEntity.ok(licenseById);
    }
    
    
    
    @GetMapping("/licenses")
    public ResponseEntity<?> getAllLicense(){    	
    	List<LicenseDTO> allLicenses = licenseServices.getAllLicenses();    	
    	return ResponseEntity.ok(allLicenses);
    }
    
    
    
    
    
    @Autowired
    LicenseRepository licenseRepository;

    /**
     * Search for licenses based on various criteria.
     *
     * @param productName   The name of the product (optional).
     * @param productVersion The version of the product (optional).
     * @param state          The state of the license (optional, defaults to 'Activate').
     * @param price          The maximum price of the license (optional, defaults to 100000000.0).
     * @param expireDate     The expiration date of the license (optional).
     * @return ResponseEntity containing a list of matching licenses.
     */
    @GetMapping("/license/search")
    public ResponseEntity<?> searchLicense(
            @RequestParam(value = "name", defaultValue = "", required = false) String productName,
            @RequestParam(value = "version", defaultValue = "", required = false) String productVersion,
            @RequestParam(value = "state", defaultValue = "Activate", required = false) String state,
            @RequestParam(value = "price", defaultValue = "100000000.0", required = false) double price,
            @RequestParam(value = "expireDate", defaultValue = "", required = false) String expireDate
    ) {
        List<License> licenseList = null;

        try {
            if (!expireDate.isEmpty()) {
                // Convert the provided date string to a Timestamp
                Timestamp expirationTimestamp = Timestamp.valueOf(expireDate + " 23:59:59");

                // Use expirationTimestamp in your query
                licenseList = licenseRepository.findByProduct_ProductNameContainingAndProductVersionContainingAndStateContainingAndPriceLessThanEqualAndExpiringDateBefore(productName, productVersion, state, price, expirationTimestamp);
            } else {
                licenseList = licenseRepository.findByProduct_ProductNameContainingAndProductVersionContainingAndStateContainingAndPriceLessThanEqual(productName, productVersion, state, price);
            }
        } catch (Exception e) {
            log.error("An error occurred while searching for licenses.", e);
            throw new InvalidDateFormatException();
        }

        return ResponseEntity.ok(licenseList);
    }
    
    
    
    public Timestamp generateFutureTimestamp(int yearsToAdd) {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        // Add the specified number of years to the current date
        LocalDateTime futureDateTime = currentDateTime.plusYears(yearsToAdd);
        
        // Convert LocalDateTime to Timestamp
        Timestamp futureTimestamp = Timestamp.from(futureDateTime.toInstant(ZoneOffset.UTC));
        
        return futureTimestamp;
    }

	
}
