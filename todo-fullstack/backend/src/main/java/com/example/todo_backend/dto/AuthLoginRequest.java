package com.example.todo_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
		@Email(message = "email must be valid")
		@NotBlank(message = "email is required")
		String email,

		@NotBlank(message = "password is required")
		String password
) {
}

