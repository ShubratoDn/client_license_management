package com.license.management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.management.entities.License;
import com.license.management.entities.Notification;
import com.license.management.entities.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Notification findByNotificationTypeAndUserAndLicense(String notificationType, User user, License license);
	
	List<Notification> findByUserOrderByNotificationIdDesc(User user);
}
