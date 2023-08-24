package com.license.management.services;

import java.util.List;

import com.license.management.DTO.ProductDTO;

public interface ProductServices {

	public ProductDTO addProduct(ProductDTO productDTO);

	public List<ProductDTO> getProductByNameAndVersion(ProductDTO productDTO);
	
	public ProductDTO getProductById(Long id);
	
	public List<ProductDTO> getAllProduct();
	
	public List<ProductDTO> getProductsBySearch(String productName, String version);
	
	public void deleteProductById(long id);
}
