package com.example.issue_backend.service;

import com.example.issue_backend.dto.AuthLoginRequest;
import com.example.issue_backend.dto.AuthRegisterRequest;
import com.example.issue_backend.dto.AuthResponse;
import com.example.issue_backend.model.AppUser;
import com.example.issue_backend.repository.AppUserRepository;
import com.example.issue_backend.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class AuthService {

	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public void register(AuthRegisterRequest request) {
		String email = request.email().trim().toLowerCase();

		if (appUserRepository.existsByEmailIgnoreCase(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "email already registered");
		}

		AppUser user = new AppUser();
		user.setEmail(email);
		user.setPasswordHash(passwordEncoder.encode(request.password()));
		user.setRole(AppUser.Role.USER);

		appUserRepository.save(user);
	}

	public AuthResponse login(AuthLoginRequest request) {
		AppUser user = appUserRepository.findByEmailIgnoreCase(request.email().trim())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials"));

		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");
		}

		String token = jwtService.createToken(
				user.getEmail(),
				Map.of("role", user.getRole().name())
		);

		return new AuthResponse(token, "Bearer");
	}
}

