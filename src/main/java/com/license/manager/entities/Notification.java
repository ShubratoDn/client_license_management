package com.license.manager.entities;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long notificationId;
	
	@ManyToOne
	private User user;	
	
	@ManyToOne
	private License license;
	
	private String notificationType;
	
	@Column(length = 5000)
	private String message;
	private Timestamp timestamp;
	
}
