const API_BASE_URL = 'http://localhost:8080';

async function apiFetch(path, options = {}) {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {}),
    },
  });

  if (!res.ok) {
    // Try to extract a useful message from the backend response
    let message = `Request failed (${res.status})`;
    try {
      const data = await res.json();
      if (data?.message) message = data.message;
    } catch (_) {
      // ignore JSON parse errors
    }
    throw new Error(message);
  }

  if (res.status === 204) return null;

  // Some endpoints return JSON immediately; if empty, return null
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

export function getTodos() {
  return apiFetch('/api/todos');
}

export function createTodo({ title, completed }) {
  return apiFetch('/api/todos', {
    method: 'POST',
    body: JSON.stringify({ title, completed }),
  });
}

export function updateTodo(id, { title, completed }) {
  return apiFetch(`/api/todos/${id}`, {
    method: 'PUT',
    body: JSON.stringify({ title, completed }),
  });
}

export function deleteTodo(id) {
  return apiFetch(`/api/todos/${id}`, { method: 'DELETE' });
}

