package com.example.issue_backend.dto;

public record AuthResponse(
		String token,
		String tokenType
) {
}

