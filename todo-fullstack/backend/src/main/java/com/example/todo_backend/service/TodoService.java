package com.example.todo_backend.service;

import com.example.todo_backend.dto.TodoCreateRequest;
import com.example.todo_backend.dto.TodoResponse;
import com.example.todo_backend.dto.TodoUpdateRequest;
import com.example.todo_backend.model.Todo;
import com.example.todo_backend.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TodoService {

	private final TodoRepository todoRepository;

	public TodoService(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	public List<TodoResponse> getAll() {
		return todoRepository.findAll().stream().map(this::toResponse).toList();
	}

	public TodoResponse getById(Long id) {
		Todo todo = todoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return toResponse(todo);
	}

	public TodoResponse create(TodoCreateRequest request) {
		Todo todo = new Todo();
		todo.setTitle(request.title().trim());
		todo.setCompleted(request.completed());

		return toResponse(todoRepository.save(todo));
	}

	public TodoResponse update(Long id, TodoUpdateRequest request) {
		Todo todo = todoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (request.title() != null) {
			String trimmedTitle = request.title().trim();
			if (trimmedTitle.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title must not be blank");
			}
			todo.setTitle(trimmedTitle);
		}

		if (request.completed() != null) {
			todo.setCompleted(request.completed());
		}

		return toResponse(todoRepository.save(todo));
	}

	public void delete(Long id) {
		if (!todoRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		todoRepository.deleteById(id);
	}

	private TodoResponse toResponse(Todo todo) {
		return new TodoResponse(todo.getId(), todo.getTitle(), todo.isCompleted());
	}
}

