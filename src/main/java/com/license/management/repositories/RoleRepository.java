package com.license.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.management.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
}
