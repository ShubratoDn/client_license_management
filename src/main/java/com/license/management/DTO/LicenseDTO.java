package com.license.management.DTO;

import java.sql.Timestamp;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseDTO {
		private Long licenseId;

	    private String licenseKey;

	    @NotNull(message = "Please select a product")
	    private ProductDTO product;

	    @PastOrPresent(message = "Activation date must be in the past or present")
	    private Timestamp activationDate;

	    @NotNull(message = "Please provide an expiring date")
	    @FutureOrPresent(message = "Expiring date must be in the future or present")
	    private Timestamp expiringDate;

	    private String state;
	    
	    @NotNull(message = "price is required")
	    @DecimalMin(value = "1.0", message = "Price must be greater than or equal to $1.0")
	    private double price;

}
