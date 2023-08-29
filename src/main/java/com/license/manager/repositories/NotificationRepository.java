package com.license.manager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.manager.entities.License;
import com.license.manager.entities.Notification;
import com.license.manager.entities.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Notification findByNotificationTypeAndUserAndLicense(String notificationType, User user, License license);
	
	List<Notification> findByUserOrderByNotificationIdDesc(User user);
}
