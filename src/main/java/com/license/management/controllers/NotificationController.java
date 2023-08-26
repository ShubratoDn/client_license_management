package com.license.management.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.license.management.DTO.NotificationDTO;
import com.license.management.DTO.UserDTO;
import com.license.management.payloads.NotificationResponse;
import com.license.management.services.NotificationService;
import com.license.management.services.UserServices;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private UserServices userServices;
	

	
	@GetMapping("/my-notifactions")
	public ResponseEntity<?> test(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();		
		UserDTO userByEmail = userServices.getUserByEmail(username);		
		List<NotificationDTO> notificationsByUser = notificationService.getNotificationsByUser(userByEmail);
		
		List<NotificationResponse> responses = new ArrayList<>();
		
		for(NotificationDTO notificationDTO: notificationsByUser) {
			NotificationResponse notificationResponse = new NotificationResponse(); 
		
			notificationResponse.setNotificationId(notificationDTO.getNotificationId());
			notificationResponse.setMessage(notificationDTO.getMessage());
			notificationResponse.setNotificationType(notificationDTO.getNotificationType());
			notificationResponse.setProductName(notificationDTO.getLicense().getProduct().getProductName());
			notificationResponse.setProductVersion(notificationDTO.getLicense().getProduct().getVersion());
			notificationResponse.setTimestamp(notificationDTO.getTimestamp());
			notificationResponse.setUsername(notificationDTO.getUser().getFullName());
			
			responses.add(notificationResponse);
		}		
		
		return ResponseEntity.ok(responses);
	}
}
