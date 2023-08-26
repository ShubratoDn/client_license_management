package com.license.management.services;

import java.util.List;

import com.license.management.DTO.NotificationDTO;
import com.license.management.DTO.UserDTO;

public interface NotificationService {
	public List<NotificationDTO> getNotificationsByUser(UserDTO userDTO); 
}
