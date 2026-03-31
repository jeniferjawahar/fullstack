package com.example.todo_backend.service;

import com.example.todo_backend.dto.IssueCreateRequest;
import com.example.todo_backend.dto.IssueResponse;
import com.example.todo_backend.dto.IssueUpdateRequest;
import com.example.todo_backend.model.Issue;
import com.example.todo_backend.repository.IssueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Service
public class IssueService {

	private final IssueRepository issueRepository;

	public IssueService(IssueRepository issueRepository) {
		this.issueRepository = issueRepository;
	}

	public List<IssueResponse> getAll() {
		return issueRepository.findAll().stream().map(this::toResponse).toList();
	}

	public IssueResponse getById(Long id) {
		Issue issue = issueRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return toResponse(issue);
	}

	public IssueResponse create(IssueCreateRequest request, Principal principal) {
		Instant now = Instant.now();

		Issue issue = new Issue();
		issue.setTitle(request.title().trim());
		issue.setDescription(request.description().trim());
		issue.setStatus(Issue.Status.OPEN);
		issue.setCreatedByEmail(principal.getName());
		issue.setCreatedAt(now);
		issue.setUpdatedAt(now);

		return toResponse(issueRepository.save(issue));
	}

	public IssueResponse update(Long id, IssueUpdateRequest request) {
		Issue issue = issueRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (request.title() != null) {
			String trimmed = request.title().trim();
			if (trimmed.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title must not be blank");
			}
			issue.setTitle(trimmed);
		}

		if (request.description() != null) {
			String trimmed = request.description().trim();
			if (trimmed.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description must not be blank");
			}
			issue.setDescription(trimmed);
		}

		if (request.status() != null) {
			issue.setStatus(request.status());
		}

		issue.setUpdatedAt(Instant.now());
		return toResponse(issueRepository.save(issue));
	}

	public void delete(Long id) {
		if (!issueRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		issueRepository.deleteById(id);
	}

	private IssueResponse toResponse(Issue issue) {
		return new IssueResponse(
				issue.getId(),
				issue.getTitle(),
				issue.getDescription(),
				issue.getStatus(),
				issue.getCreatedByEmail(),
				issue.getCreatedAt(),
				issue.getUpdatedAt()
		);
	}
}

