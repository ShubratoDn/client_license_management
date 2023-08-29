package com.license.manager.payloads;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse {

	private Long notificationId;
	private String username;
	private String productName;
	private String productVersion;
	private String notificationType;
	private String message;
	private Timestamp timestamp;
}
