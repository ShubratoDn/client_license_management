package com.license.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.management.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findByUsernameOrEmail(String username, String email );
	
}
