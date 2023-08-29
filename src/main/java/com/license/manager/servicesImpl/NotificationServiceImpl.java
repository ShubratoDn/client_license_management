package com.license.manager.servicesImpl;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.license.manager.DTO.NotificationDTO;
import com.license.manager.DTO.UserDTO;
import com.license.manager.config.Constants;
import com.license.manager.entities.License;
import com.license.manager.entities.Notification;
import com.license.manager.entities.User;
import com.license.manager.entities.UserLicense;
import com.license.manager.repositories.NotificationRepository;
import com.license.manager.repositories.UserLicenseRepository;
import com.license.manager.services.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private UserLicenseRepository userLicenseRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	
	 /**
     * Find user licenses expiring soon and send notifications.
     * 
     * This method calculates the expiration time for licenses (current time + 24
     * hours) and checks for user licenses that are either deactivated or expiring
     * within the next 24 hours. It then sends notifications to users for expired
     * licenses or licenses that will expire soon.
     */
	
	@Scheduled(fixedRate = 1000 * 60 * 1) 
	public void sendNotificationAutomatically() {
        // Calculate the expiration time (current time + given hours)
        Instant now = Instant.now();
        Instant expirationInstant = now.plus(Duration.ofHours(24)); // Add 24 hours to the current time

        Timestamp expirationTime = Timestamp.from(expirationInstant);

        List<UserLicense> userLicenses = userLicenseRepository.findByLicenseStateOrLicenseExpiringDateBeforeAndIsActiveIsTrue("Deactivate",expirationTime);
      
        for(UserLicense userLicense : userLicenses ) {   
        	// Create a notification for each user license
        	NotificationDTO check = null;
        	
    		Notification notification = new Notification();
    		notification.setLicense(userLicense.getLicense());
    		notification.setUser(userLicense.getUser());
      		notification.setTimestamp(Timestamp.from(Instant.now()));
        	
        	if(userLicense.getLicense().getState().equalsIgnoreCase("deactivate")) {
        		check = checkSentNotificationForNotificationType(userLicense.getUser(), userLicense.getLicense(), Constants.NOTIFICATION_TYPE_EXPIRED);
        		notification.setNotificationType(Constants.NOTIFICATION_TYPE_EXPIRED);
        		notification.setMessage(this.expireSoonMessage(userLicense));
        	}else {
        		check = checkSentNotificationForNotificationType(userLicense.getUser(), userLicense.getLicense(), Constants.NOTIFICATION_TYPE_EXPIRE_SOON);
        		notification.setNotificationType(Constants.NOTIFICATION_TYPE_EXPIRE_SOON);
        		notification.setMessage(this.exipredMessage(userLicense));
        	}
        	
        	// Save the notification
        	if(check == null) {        		
        		notificationRepository.save(notification);        		
        		 log.info("Notification added for user: {} and license: {}", userLicense.getUser().getUsername(),
                         userLicense.getLicense().getLicenseId());
        	}
        }
    }
	
	
	
	/**
     * Check if a notification of a given type has been sent for a user and license.
     * 
     * @param user    The user for whom the notification is checked.
     * @param license The license for which the notification is checked.
     * @param type    The type of notification to check (e.g., expired or expiring
     *                soon).
     * @return The notification if it exists, otherwise null.
     */
	private NotificationDTO checkSentNotificationForNotificationType(User user, License license, String type) {
		Notification findByNotificationTypeAndUserAndLicense = notificationRepository.findByNotificationTypeAndUserAndLicense(type, user, license);
		if(findByNotificationTypeAndUserAndLicense == null) {
			return null;
		}
		return modelMapper.map(findByNotificationTypeAndUserAndLicense, NotificationDTO.class);
	}
	
	
	
	private String expireSoonMessage(UserLicense userLicense) {
		return "Hey "+userLicense.getUser().getFullName()+", \r\n"
				+ "\r\n"
				+ "Your license for "+userLicense.getLicense().getProduct().getProductName()+" is expiring soon. Please renew to continue using our services.\r\n"
				+ "\r\n"
				+ "License Details:\r\n"
				+ "- License Key: "+userLicense.getLicense().getLicenseKey()+"\r\n"
				+ "- Expiration Date: "+userLicense.getLicense().getExpiringDate()+"\r\n"
				+ "\r\n"
				+ "Thanks for choosing us!\r\n"
				+ "\r\n"
				+ "Best regards,\r\n"
				+ "Software License Management\r\n"
				+ "This shorter message still conveys the important information about the expiring license and provides key details for the user.\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "";
	}
	
	
	private String exipredMessage(UserLicense userLicense) {
		return "Hey "+userLicense.getUser().getFullName()+",\r\n"
				+ "\r\n"
				+ "We regret to inform you that your license for "+userLicense.getLicense().getProduct().getProductName()+" has expired. To continue using our services, please renew your license.\r\n"
				+ "\r\n"
				+ "License Details:\r\n"
				+ "- License Key: "+userLicense.getLicense().getLicenseKey()+"\r\n"
				+ "- Expiration Date: "+userLicense.getLicense().getExpiringDate()+"\r\n"
				+ "\r\n"
				+ "If you have any questions or need assistance, please don't hesitate to reach out to our support team.\r\n"
				+ "\r\n"
				+ "Best regards,\r\n"
				+ "Software License Management\r\n"
				+ "";
	}



	@Override
	public List<NotificationDTO> getNotificationsByUser(UserDTO userDTO) {		
		List<Notification> notifications = notificationRepository.findByUserOrderByNotificationIdDesc(modelMapper.map(userDTO, User.class));
		
		List<NotificationDTO> notificationDTOs = new ArrayList<>(); 
		
		for(Notification notification : notifications) {
			notificationDTOs.add(modelMapper.map(notification, NotificationDTO.class));
		}
		return notificationDTOs;
	}
	
	
}


