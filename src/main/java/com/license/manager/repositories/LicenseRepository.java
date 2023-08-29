package com.license.manager.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.manager.entities.License;
import com.license.manager.entities.Product;

public interface LicenseRepository extends JpaRepository<License, Long> {

	 // Conventionally named method to find a Product by attributes
    License findByProductAndExpiringDateAndPrice(Product product, Timestamp expiringDate, double price);
	
        
    List<License> findByExpiringDateBefore(Timestamp expiringDate);
    
    
    
    List<License> findByProduct_ProductNameContainingAndProductVersionContainingAndStateContainingAndPriceLessThanEqual(String productName, String productVersion, String state, double price);
    List<License> findByProduct_ProductNameContainingAndProductVersionContainingAndStateContainingAndPriceLessThanEqualAndExpiringDateBefore(String productName, String productVersion, String state, double price, Timestamp expiringDate);
    
    
    
}

