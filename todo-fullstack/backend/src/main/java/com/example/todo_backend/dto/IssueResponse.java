package com.example.todo_backend.dto;

import com.example.todo_backend.model.Issue;

import java.time.Instant;

public record IssueResponse(
		Long id,
		String title,
		String description,
		Issue.Status status,
		String createdByEmail,
		Instant createdAt,
		Instant updatedAt
) {
}

