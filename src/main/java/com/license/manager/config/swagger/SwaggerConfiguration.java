package com.license.manager.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {

	private SecurityScheme createAPIKeyScheme() {
	    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
	        .bearerFormat("JWT")
	        .scheme("bearer");
	}
	
	
	@Bean
	public OpenAPI openAPI() {
	    return new OpenAPI().addSecurityItem(new SecurityRequirement().
	            addList("Bearer Authentication"))
	        .components(new Components().addSecuritySchemes
	            ("Bearer Authentication", createAPIKeyScheme()))
	        .info(new Info().title("REST API's for Software License Management")
	            .description("Try this API's")
	            .version("1.0").contact(new Contact().name("Cambridge Experts")
	                .email( "info@cambridge-experts.co.uk").url("https://www.cambridge-experts.co.uk"))
	            .license(new License().name("License of API")
	                .url("API license URL")));
	}
	
}
