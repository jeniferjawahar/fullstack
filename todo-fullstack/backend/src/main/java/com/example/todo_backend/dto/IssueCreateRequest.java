package com.example.todo_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IssueCreateRequest(
		@NotBlank(message = "title is required")
		@Size(max = 200, message = "title must be <= 200 characters")
		String title,

		@NotBlank(message = "description is required")
		@Size(max = 4000, message = "description must be <= 4000 characters")
		String description
) {
}

