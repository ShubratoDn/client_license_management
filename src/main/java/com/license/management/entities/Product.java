package com.license.management.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	private String productName;
	private String version;
	@Column(length = 5000)
	private String description;	
}
