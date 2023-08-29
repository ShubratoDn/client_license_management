package com.license.manager.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.license.manager.DTO.ProductDTO;
import com.license.manager.entities.Product;
import com.license.manager.exceptions.ResourceNotFoundException;
import com.license.manager.payloads.PageableResponse;
import com.license.manager.repositories.ProductRepository;
import com.license.manager.services.ProductServices;

@Service
public class ProductServicesImpl implements ProductServices {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	
	//adding new product
	@Override
	public ProductDTO addProduct(ProductDTO productDTO) {		
		Product product = modelMapper.map(productDTO, Product.class);
		Product save = productRepository.save(product);
		
		ProductDTO mapedProduct = modelMapper.map(save, ProductDTO.class);		
		
		return mapedProduct;
	}

	
	
	//get product by Product's version and Name
	public List<ProductDTO> getProductByNameAndVersion(ProductDTO productDTO) {		
		List<Product> products = productRepository.findByProductNameAndVersion(productDTO.getProductName().trim(), productDTO.getVersion().trim());

	    if (products == null || products.isEmpty()) {
	        return new ArrayList<>(); // Return an empty list if no products are found
	    }

	    // Use Java Streams to map the products to ProductDTO
	    List<ProductDTO> productDTOs = products.stream()
	            .map(product -> modelMapper.map(product, ProductDTO.class))
	            .collect(Collectors.toList());

	    return productDTOs;
	}



	@Override
	public ProductDTO getProductById(Long id) {		
		Product product = productRepository.findById(id).orElse(null);		
		if(product== null) {
			throw new ResourceNotFoundException("Porduct", id.toString());			
		}		
		ProductDTO mapedProduct = modelMapper.map(product, ProductDTO.class);		
		return mapedProduct;
	}



	@Override
	public List<ProductDTO> getAllProduct() {
	    List<Product> findAll = productRepository.findAll();
	    
	    // Convert the list of Product entities to ProductDTOs
	    List<ProductDTO> productDTOs = findAll.stream()
	            .map(product -> modelMapper.map(product, ProductDTO.class))
	            .collect(Collectors.toList());

	    return productDTOs;
	}

	
	public void deleteProductById(long id) {
		productRepository.deleteById(id);
	}




	@Override
	public PageableResponse getAllProductPageable(int pageNumber, int pageSize, String sortBy, String sortDirection) {

		Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		Page<Product> pageInfo;
		try {
			Pageable page = PageRequest.of(pageNumber, pageSize, sort);
			pageInfo = productRepository.findAll(page);
		} catch (Exception e) {
			Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("productId").descending());
			pageInfo = productRepository.findAll(page);
		}
		
		
		List<Product> allProducts = pageInfo.getContent();		

		// Convert the list of Product entities to ProductDTOs
		List<ProductDTO> productDTOs = allProducts.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());
		
		
		PageableResponse pageData = new PageableResponse();
		pageData.setContent(productDTOs);
		pageData.setPageNumber(pageInfo.getNumber());
		pageData.setPageSize(pageInfo.getSize());
		pageData.setTotalElements(pageInfo.getTotalElements());
		pageData.setTotalPages(pageInfo.getTotalPages());
		pageData.setNumberOfElements(pageInfo.getNumberOfElements());

		pageData.setEmpty(pageInfo.isEmpty());
		pageData.setFirst(pageInfo.isFirst());
		pageData.setLast(pageInfo.isLast());
		

		
		return pageData;
	}


	
	public List<ProductDTO> getProductsBySearch(String productName, String version) {
//	    // Trim the input parameters
//	    productName = productName.trim();
//	    version = version.trim();
//
//	    // Search for products by productName and version using repository method
//	    List<Product> products = productRepository.findByProductNameContainingAndVersionContaining(productName, version);
//
//	    // Use Java Streams to map the products to ProductDTO
//	    List<ProductDTO> productDTOs = products.stream()
//	            .map(product -> modelMapper.map(product, ProductDTO.class))
//	            .collect(Collectors.toList());

	    return null;
	}
	

	@Override
	public PageableResponse getProductsBySearchPageable(String productName, String version, int pageNumber, int pageSize, String sortBy, String sortDirection) {
	    // Trim the input parameters
	    productName = productName.trim();
	    version = version.trim();

	    
	    Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		
		Page<Product> pageInfo;
		try {
			Pageable page = PageRequest.of(pageNumber, pageSize, sort);
			pageInfo = productRepository.findByProductNameContainingAndVersionContaining(productName, version, page);
		} catch (Exception e) {
			Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("productId").descending());
			pageInfo = productRepository.findByProductNameContainingAndVersionContaining(productName, version, page);
		}
		
		
		List<Product> products = pageInfo.getContent();
		
	    // Use Java Streams to map the products to ProductDTO
	    List<ProductDTO> productDTOs = products.stream()
	            .map(product -> modelMapper.map(product, ProductDTO.class))
	            .collect(Collectors.toList());
	    
		PageableResponse pageData = new PageableResponse();
		pageData.setContent(productDTOs);
		pageData.setPageNumber(pageInfo.getNumber());
		pageData.setPageSize(pageInfo.getSize());
		pageData.setTotalElements(pageInfo.getTotalElements());
		pageData.setTotalPages(pageInfo.getTotalPages());
		pageData.setNumberOfElements(pageInfo.getNumberOfElements());

		pageData.setEmpty(pageInfo.isEmpty());
		pageData.setFirst(pageInfo.isFirst());
		pageData.setLast(pageInfo.isLast());
		
		return pageData;
	}


	
}
