package com.example.todo_backend.dto;

public record TodoUpdateRequest(
	String title,
	Boolean completed
) {
}

