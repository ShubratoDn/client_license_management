package com.license.management.entities;

import java.sql.Timestamp;

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
public class License {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long licenseId;
	private String licenseKey;
	private Product product;
	private Timestamp activationDate;
	private Timestamp expiringDate;
	private String state;
	
}
