package com.license.manager.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.license.manager.DTO.UserDTO;
import com.license.manager.payloads.NotificationRequest;
import com.license.manager.payloads.NotificationResponse;
import com.license.manager.payloads.PageableResponse;
import com.license.manager.services.NotificationService;
import com.license.manager.services.UserServices;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
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
	@GetMapping("/notification")
	public ResponseEntity<?> getMyNotifications(			
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size,
			@RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection
			) {
		
		if(size == 0) {
			size= 5;
		}
		
	    // Get the username of the currently authenticated user
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // Retrieve user information by email
	    UserDTO userByEmail = userServices.getUserByEmail(username);

	    // Retrieve notifications for the user
	    PageableResponse notificationsByUserPageable = notificationService.getNotificationsByUserPageable(userByEmail, page, size, sortBy, sortDirection);


	    // Return the list of NotificationResponse objects as a ResponseEntity
	    return ResponseEntity.ok(notificationsByUserPageable);
	}

	
	/**
     * Update a notification with the specified ID.
     *
     * @param notificationRequest The updated notification data.
     * @param notificationId      The ID of the notification to update.
     * @return ResponseEntity containing the updated notification.
     */
	//updating notification	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/notification/{notificationId}")
	public ResponseEntity<?> updateNotification(@Valid @RequestBody NotificationRequest notificationRequest, @PathVariable Long notificationId){
		NotificationResponse updateNotification = notificationService.updateNotification(notificationId, notificationRequest);
		return ResponseEntity.ok(updateNotification);
	}
	

	
	
	/**
     * Delete a notification with the specified ID.
     *
     * @param notificationId The ID of the notification to delete.
     * @return ResponseEntity with a success message.
     */
	//deleting notification
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@DeleteMapping("/notification/{notificationId}")
	public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId){
		log.info("Notification with ID: {}", notificationId);

		notificationService.deleteNotification(notificationId);		
		
		log.warn("Notification with ID {} has been Deleted.", notificationId);
		return ResponseEntity.ok("Notification has been deleted!!");
	}
	
	
	
}
