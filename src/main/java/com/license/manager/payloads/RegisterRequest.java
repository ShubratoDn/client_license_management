package com.license.manager.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	
	@NotBlank(message = "Username is required")
	@Size(min = 3, message = "Username must be at least 3 characters long")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Full name is required")
	@Size(min = 3, message = "Name must be at least 3 characters long")
    private String fullName;

    private String secretCode;
	
}
