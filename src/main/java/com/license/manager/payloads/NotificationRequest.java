package com.license.manager.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {

	@NotBlank(message = "Notification type is required")
	private String notificationType;
	@NotBlank(message = "Message is required")
	private String message;
	
}
