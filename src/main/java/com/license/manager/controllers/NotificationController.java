package com.license.manager.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.license.manager.DTO.NotificationDTO;
import com.license.manager.DTO.UserDTO;
import com.license.manager.payloads.NotificationResponse;
import com.license.manager.services.NotificationService;
import com.license.manager.services.UserServices;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private UserServices userServices;
	

	
	/**
	 * Retrieves notifications for the currently authenticated user.
	 *
	 * @return A ResponseEntity containing a list of NotificationResponse objects.
	 */
	@GetMapping("/my-notifactions")
	public ResponseEntity<?> getMyNotifications() {
	    // Get the username of the currently authenticated user
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // Retrieve user information by email
	    UserDTO userByEmail = userServices.getUserByEmail(username);

	    // Retrieve notifications for the user
	    List<NotificationDTO> notificationsByUser = notificationService.getNotificationsByUser(userByEmail);

	    // Prepare a list of NotificationResponse objects
	    List<NotificationResponse> responses = new ArrayList<>();

	    // Map NotificationDTOs to NotificationResponse objects
	    for (NotificationDTO notificationDTO : notificationsByUser) {
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

	    // Return the list of NotificationResponse objects as a ResponseEntity
	    return ResponseEntity.ok(responses);
	}

}
