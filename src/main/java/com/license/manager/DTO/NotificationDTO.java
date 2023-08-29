package com.license.manager.DTO;

import java.sql.Timestamp;

import com.license.manager.entities.License;
import com.license.manager.entities.User;

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
