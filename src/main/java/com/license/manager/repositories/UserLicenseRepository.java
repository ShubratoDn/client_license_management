package com.license.manager.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.manager.entities.License;
import com.license.manager.entities.User;
import com.license.manager.entities.UserLicense;

public interface UserLicenseRepository extends JpaRepository<UserLicense, Long> {

	UserLicense findByUserAndLicense(User user, License license);
	
	List<UserLicense> findByUserOrderByUserLicenseIdDesc(User user);
	
	List<UserLicense> findByLicenseStateOrLicenseExpiringDateBeforeAndIsActiveIsTrue(String state, Timestamp expirationTime);
}
