package com.license.management.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.management.entities.License;
import com.license.management.entities.Product;

public interface LicenseRepository extends JpaRepository<License, Long> {

	 // Conventionally named method to find a Product by attributes
    License findByProductAndExpiringDateAndPrice(Product product, Timestamp expiringDate, double price);
	
}
