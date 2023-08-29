package com.license.manager.payloads;

import com.license.manager.entities.License;

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
