package com.license.manager;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.license.manager.config.Constants;
import com.license.manager.entities.Role;
import com.license.manager.repositories.RoleRepository;


@SpringBootApplication
@EnableScheduling
public class LicenseManagerApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(LicenseManagerApplication.class, args);
	}
	
	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		
		//Adding roles
		Role roleUser = new Role();
		roleUser.setId(Constants.ROLE_USER);
		roleUser.setRole("ROLE_USER");
		
		Role roleAdmin = new Role();
		roleAdmin.setId(Constants.ROLE_ADMIN);
		roleAdmin.setRole("ROLE_ADMIN");
		
		List<Role> roles = List.of(roleUser, roleAdmin);		
		roleRepo.saveAll(roles);	
		
	}	
}
