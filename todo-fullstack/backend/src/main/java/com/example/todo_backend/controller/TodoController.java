package com.example.todo_backend.controller;

import com.example.todo_backend.dto.TodoCreateRequest;
import com.example.todo_backend.dto.TodoResponse;
import com.example.todo_backend.dto.TodoUpdateRequest;
import com.example.todo_backend.service.TodoService;
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

import jakarta.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
@RequestMapping("/api/todos")
public class TodoController {

	private final TodoService todoService;

	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping
	public List<TodoResponse> getAll() {
		return todoService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<TodoResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(todoService.getById(id));
	}

	@PostMapping
	public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoCreateRequest request) {
		return ResponseEntity.ok(todoService.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoUpdateRequest request) {
		return ResponseEntity.ok(todoService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		todoService.delete(id);
		return ResponseEntity.noContent().build();
	}
}

