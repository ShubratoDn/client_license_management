package com.license.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.manager.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
}
