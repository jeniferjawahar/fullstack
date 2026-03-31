package com.example.todo_backend.dto;

public record AuthResponse(
		String token,
		String tokenType
) {
}

