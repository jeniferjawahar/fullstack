package com.example.todo_backend.dto;

public record TodoResponse(
	Long id,
	String title,
	boolean completed
) {
}

