package com.license.management.DTO;

import java.sql.Timestamp;

import com.license.management.entities.License;
import com.license.management.entities.User;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {	
	private Long notificationId;

	private User user;	

	private License license;
	
	private String notificationType;
	private String message;
	private Timestamp timestamp;
}
