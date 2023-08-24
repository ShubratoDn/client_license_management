package com.license.management.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Long productId;
	
	@NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Version is required")
    private String version;

    @NotBlank(message = "Description is required")
    private String description;
}
