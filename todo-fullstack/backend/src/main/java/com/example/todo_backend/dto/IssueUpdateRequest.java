package com.example.todo_backend.dto;

import com.example.todo_backend.model.Issue;

import jakarta.validation.constraints.Size;

public record IssueUpdateRequest(
		@Size(max = 200, message = "title must be <= 200 characters")
		String title,

		@Size(max = 4000, message = "description must be <= 4000 characters")
		String description,

		Issue.Status status
) {
}

