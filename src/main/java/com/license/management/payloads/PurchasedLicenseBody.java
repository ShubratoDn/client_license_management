package com.license.management.payloads;

import com.license.management.entities.License;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PurchasedLicenseBody {
	private Long userLicenseId;

    private String username;
    
    private boolean isActive;
    
    private License license;
	
}
