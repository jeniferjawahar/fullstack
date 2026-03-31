package com.example.todo_backend.controller;

import com.example.todo_backend.dto.IssueCreateRequest;
import com.example.todo_backend.dto.IssueResponse;
import com.example.todo_backend.dto.IssueUpdateRequest;
import com.example.todo_backend.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/issues")
public class IssueController {

	private final IssueService issueService;

	public IssueController(IssueService issueService) {
		this.issueService = issueService;
	}

	@GetMapping
	public List<IssueResponse> getAll() {
		return issueService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<IssueResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(issueService.getById(id));
	}

	@PostMapping
	public ResponseEntity<IssueResponse> create(@Valid @RequestBody IssueCreateRequest request, Principal principal) {
		return ResponseEntity.ok(issueService.create(request, principal));
	}

	@PutMapping("/{id}")
	public ResponseEntity<IssueResponse> update(@PathVariable Long id, @Valid @RequestBody IssueUpdateRequest request) {
		return ResponseEntity.ok(issueService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		issueService.delete(id);
		return ResponseEntity.noContent().build();
	}
}

