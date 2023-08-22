package com.license.management.entities;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long notificationId;
	private User user;
	private String notificationType;
	private String message;
	private Timestamp timestamp;
	
}
