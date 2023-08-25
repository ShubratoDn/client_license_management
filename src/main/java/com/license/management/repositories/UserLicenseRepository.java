package com.license.management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.management.entities.License;
import com.license.management.entities.User;
import com.license.management.entities.UserLicense;

public interface UserLicenseRepository extends JpaRepository<UserLicense, Long> {

	UserLicense findByUserAndLicense(User user, License license);
	
	List<UserLicense> findByUser(User user);
}