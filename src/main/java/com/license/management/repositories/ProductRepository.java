package com.license.management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.management.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByProductNameAndVersion(String productname, String version);
	
	List<Product> findByProductNameContainingAndVersionContaining(String productName, String version);

	
}
