package com.license.manager.services;

import java.util.List;

import com.license.manager.DTO.ProductDTO;
import com.license.manager.payloads.PageableResponse;

public interface ProductServices {

	public ProductDTO addProduct(ProductDTO productDTO);

	public List<ProductDTO> getProductByNameAndVersion(ProductDTO productDTO);
	
	public ProductDTO getProductById(Long id);
	
	public List<ProductDTO> getAllProduct();
	
	public PageableResponse getAllProductPageable(int pageNumber, int pageSize, String sortBy, String sortDirection);
	
	public List<ProductDTO> getProductsBySearch(String productName, String version);
	public PageableResponse getProductsBySearchPageable(String productName, String version,int pageNumber, int pageSize, String sortBy, String sortDirection);
	
	public void deleteProductById(long id);
	
	
	public ProductDTO updateProduct(Long productId, ProductDTO productDTO);
}
