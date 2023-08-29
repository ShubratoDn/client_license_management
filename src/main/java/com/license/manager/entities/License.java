package com.license.manager.entities;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class License {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long licenseId;
	private String licenseKey;
	
	@ManyToOne
	@JoinColumn(name = "product_id") // This is the foreign key column in the License table	
	private Product product;
	
	private Timestamp activationDate;
	private Timestamp expiringDate;
	private String state;	
	private double price;
}
