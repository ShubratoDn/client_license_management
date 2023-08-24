package com.license.management.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.license.management.DTO.ProductDTO;
import com.license.management.entities.Product;
import com.license.management.repositories.ProductRepository;
import com.license.management.services.ProductServices;

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
			return null;
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



	public List<ProductDTO> getProductsBySearch(String productName, String version) {
	    // Trim the input parameters
	    productName = productName.trim();
	    version = version.trim();

	    // Search for products by productName and version using repository method
	    List<Product> products = productRepository.findByProductNameContainingAndVersionContaining(productName, version);

	    // Use Java Streams to map the products to ProductDTO
	    List<ProductDTO> productDTOs = products.stream()
	            .map(product -> modelMapper.map(product, ProductDTO.class))
	            .collect(Collectors.toList());

	    return productDTOs;
	}


	
}
