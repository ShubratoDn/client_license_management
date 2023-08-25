package com.license.management.servicesImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.license.management.DTO.LicenseDTO;
import com.license.management.DTO.ProductDTO;
import com.license.management.entities.License;
import com.license.management.entities.Product;
import com.license.management.exceptions.ResourceNotFoundException;
import com.license.management.repositories.LicenseRepository;
import com.license.management.services.LicenseServices;
import com.license.management.services.ProductServices;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LicenseServicesImpl implements LicenseServices {
	
	
	//add license
	//delete license
	//update license (Active / Deactivate)
	//Who purchased license
	//ekta license kon kon user purchase korse 

	@Autowired
	private ProductServices productServices;
	
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
     * Generates a random license key of the specified length.
     *
     * @param length The length of the license key to generate.
     * @return A randomly generated license key.
     */
	private String generateLiceseKey(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
	
	
	/**
     * Adds a new license.
     *
     * @param licenseDTO The LicenseDTO object containing license details.
     * @return The added LicenseDTO.
     * @throws ResourceNotFoundException If the associated product is not found.
     */
	@Override
	public LicenseDTO addLicense(LicenseDTO licenseDTO) {
	    // Retrieve the associated product by its ID
		ProductDTO product = productServices.getProductById(licenseDTO.getProduct().getProductId());
		if(product == null) {
			throw new ResourceNotFoundException("Product", "id "+licenseDTO.getProduct().getProductId().toString());
		}
		
		// Normalize the state to "Active" or "Deactive"
		if(licenseDTO.getState().equalsIgnoreCase("deactivate")) {
			licenseDTO.setState("Deactivate");
		}else {
			licenseDTO.setState("Activate");
		}
		
		 // Set additional properties for the license
		licenseDTO.setProduct(product);
		licenseDTO.setActivationDate(Timestamp.from(Instant.now()));
		licenseDTO.setLicenseKey(this.generateLiceseKey(50));		
		 // Save the license and map it to DTO
		License save = licenseRepository.save(modelMapper.map(licenseDTO, License.class));
		LicenseDTO mappedLicense = modelMapper.map(save, LicenseDTO.class);	
		
		 // Log that the license has been added
        log.info("Added a new license with ID: {}", mappedLicense.getLicenseId());

		return mappedLicense;
	}


	
	/**
	 * Matches a license based on the specified product, expiration date, and price.
	 *
	 * @param licenseDTO The LicenseDTO containing product, expiration date, and price.
	 * @return A LicenseDTO representing the matched license, or null if no match is found.
	 */
	@Override
	public LicenseDTO matchLicese(LicenseDTO licenseDTO) {
		// Map LicenseDTO's product to a Product entity
		Product product = modelMapper.map(licenseDTO.getProduct(), Product.class);
		// Query the repository for a matching license
		License matchedLicense = licenseRepository.findByProductAndExpiringDateAndPrice(product, licenseDTO.getExpiringDate(), licenseDTO.getPrice());
		// If no match is found, return null; otherwise, map and return the matched license as LicenseDTO
		if(matchedLicense == null) {
			return null;
		}
		return modelMapper.map(matchedLicense, LicenseDTO.class);
	}
	
	
	
	
	public LicenseDTO getLicenseById(Long licenseId){
		License license = licenseRepository.findById(licenseId).orElseThrow(()-> new ResourceNotFoundException("License", licenseId.toString()));
		return modelMapper.map(license, LicenseDTO.class);		
	}
	
	

	public void deleteLicense(Long licenseId) {
		licenseRepository.deleteById(licenseId);
	}
	
	
	public LicenseDTO updateLicense(LicenseDTO licenseDTO) {		
		// Normalize the state to "Active" or "Deactive"
		if(licenseDTO.getState().equalsIgnoreCase("deactivate")) {
			licenseDTO.setState("Deactivate");
		}else {
			licenseDTO.setState("Activate");
		}
		
		License license = modelMapper.map(licenseDTO, License.class);		
		License save = licenseRepository.save(license);
		
		return modelMapper.map(save, LicenseDTO.class);
	}

	
	
	public List<LicenseDTO> getAllLicenses(){
		List<License> findAll = licenseRepository.findAll();
		
		List<LicenseDTO> licenseDTOs = findAll.stream()
				.map(license -> modelMapper.map(license, LicenseDTO.class))
	            .collect(Collectors.toList());
		return licenseDTOs;
		
	}
	
}
