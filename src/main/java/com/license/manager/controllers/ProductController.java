package com.license.manager.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.license.manager.DTO.ProductDTO;
import com.license.manager.payloads.ErrorResponse;
import com.license.manager.payloads.PageableResponse;
import com.license.manager.services.ProductServices;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProductController {
	
	@Autowired
	private ProductServices productServices;	
	/**
	 * Adds a new product to the system. Only users with the 'ROLE_ADMIN' role are allowed to access this endpoint.
	 *
	 * @param productDTO The product data to be added.
	 * @return ResponseEntity containing the added product or an error response if the operation fails.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO) {
	    log.info("Adding a new product: {}", productDTO);

	    List<ProductDTO> productByNameAndVersion = productServices.getProductByNameAndVersion(productDTO);

	    if (productByNameAndVersion.size() == 1) {
	        log.error("Product already exists with the same name and version: {}", productDTO);
	        ErrorResponse errorResponse = new ErrorResponse(
	                LocalDateTime.now(),
	                HttpStatus.CONFLICT.value(),
	                "Product Already Exists",
	                "The product with the given name and version already exists."
	        );
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	    }

	    log.debug("Product not found with the same name and version. Proceeding to add: {}", productDTO);
	    ProductDTO addProduct = productServices.addProduct(productDTO);

	    if (addProduct == null) {
	        log.error("Failed to add product: {}", productDTO);
	        ErrorResponse errorResponse = new ErrorResponse(
	                LocalDateTime.now(),
	                HttpStatus.BAD_REQUEST.value(),
	                "Product Not Added",
	                "The product was not added. Please check your request."
	        );
	        return ResponseEntity.badRequest().body(errorResponse);
	    }

	    log.info("Product added successfully: {}", addProduct);
	    return ResponseEntity.ok(addProduct);
	}

	
	
	
	
	/**
	 * Deletes a product with the specified ID from the system. Only users with the 'ROLE_ADMIN' role are allowed to access this endpoint.
	 *
	 * @param productId The ID of the product to be deleted.
	 * @return ResponseEntity with a success message or an error response if the product is not found.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/product/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable long productId) {
	    log.info("Deleting product with ID: {}", productId);

	    ProductDTO productById = productServices.getProductById(productId);
	    
	    if (productById == null) {
	        log.error("Product not found with ID: {}", productId);
	        ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            "Product Not Found",
	            "Product with ID " + productId + " not found."
	        );
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	    }

	    productServices.deleteProductById(productId);

	    log.info("Product with ID {} has been deleted.", productId);
	    return ResponseEntity.ok("Product with ID " + productId + " has been deleted.");
	}

	
	
	
	
	/**
	 * Retrieves a product by its unique ID.
	 *
	 * @param productId The ID of the product to retrieve.
	 * @return ResponseEntity containing the product information if found, or an error response if the product is not found.
	 */
	@GetMapping("/product/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable long productId) {
	    ProductDTO productById = productServices.getProductById(productId);
	    if (productById == null) {
	        log.error("Product not found with ID: {}", productId);
	        ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            "Product Not Found",
	            "Product with ID " + productId + " not found."
	        );
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	    }
	    return ResponseEntity.ok(productById);
	}

	/**
	 * Retrieves a list of all products available in the system.
	 *
	 * @return ResponseEntity containing a list of all products.
	 */
	@GetMapping("/product")
	public ResponseEntity<?> getAllProduct(				
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size,
			@RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection
			
			) {
	    log.info("Retrieving all products");	    
	    if(size == 0) {
			size= 5;
		}
	    PageableResponse allProductPageable = productServices.getAllProductPageable(page, size, sortBy, sortDirection);
	    return ResponseEntity.ok(allProductPageable);
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Retrieves a list of products that match the specified criteria.
	 *
	 * @param productName The name of the product to search for (optional).
	 * @param version The version of the product to search for (optional).
	 * @return ResponseEntity containing a list of products that match the search criteria.
	 */
	@GetMapping("/product/search")
	public ResponseEntity<?> getAllProductBySearch(
	        @RequestParam(value = "name", defaultValue = "", required = false) String productName,
	        @RequestParam(value = "version", defaultValue = "", required = false) String version,
	    	@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size,
			@RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection
	) {
	    log.info("Searching for products with name: '{}' and version: '{}'", productName, version);
	    PageableResponse productsBySearchPageable = productServices.getProductsBySearchPageable(productName, version, page, size, sortBy, sortDirection);
	    return ResponseEntity.ok(productsBySearchPageable);
	}

	
	
	
	
	
	//update Product
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/product/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO){
		ProductDTO updateProduct = productServices.updateProduct(productId, productDTO);
		return ResponseEntity.ok(updateProduct);
	}
	
	
}
