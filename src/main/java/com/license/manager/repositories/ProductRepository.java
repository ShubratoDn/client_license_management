package com.license.manager.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.license.manager.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByProductNameAndVersion(String productname, String version);
	
//	List<Product> findByProductNameContainingAndVersionContaining(String productName, String version);

	Page<Product> findByProductNameContainingAndVersionContaining(String productName, String version,  Pageable page);
}
