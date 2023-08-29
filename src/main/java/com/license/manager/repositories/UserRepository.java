package com.license.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.manager.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	User findByUsername(String username);
	User findByUsernameOrEmail(String username, String email );
	
}
