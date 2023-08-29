package com.license.manager.services;

import java.util.List;

import com.license.manager.DTO.NotificationDTO;
import com.license.manager.DTO.UserDTO;
import com.license.manager.payloads.NotificationRequest;
import com.license.manager.payloads.NotificationResponse;
import com.license.manager.payloads.PageableResponse;

public interface NotificationService {
	public List<NotificationDTO> getNotificationsByUser(UserDTO userDTO);
	public PageableResponse  getNotificationsByUserPageable(UserDTO userDTO, int pageNumber, int pageSize, String sortBy, String sortDirection);
	
	public NotificationResponse updateNotification(Long notificationId, NotificationRequest notificationRequest);
	
	public void deleteNotification(long notificationId);
}
