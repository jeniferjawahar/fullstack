package com.example.todo_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record TodoCreateRequest(
	@NotBlank(message = "title is required")
	String title,
	boolean completed
) {
}

