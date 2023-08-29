package com.license.manager.services;

import java.util.List;

import com.license.manager.DTO.NotificationDTO;
import com.license.manager.DTO.UserDTO;

public interface NotificationService {
	public List<NotificationDTO> getNotificationsByUser(UserDTO userDTO); 
}
