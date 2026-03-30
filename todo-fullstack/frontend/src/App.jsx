import { useEffect, useMemo, useState } from 'react';
import './App.css';
import {
  createTodo,
  deleteTodo,
  getTodos,
  updateTodo,
} from './api/todoApi';

export default function App() {
  const [todos, setTodos] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const [title, setTitle] = useState('');
  const [completed, setCompleted] = useState(false);
  const [editingId, setEditingId] = useState(null);

const formMode=useMemo(()=>{return editingId===null ? 'create' : 'edit'},[editingId]);

  async function refreshTodos() {
    setLoading(true);
    setError('');
    try {
      const data = await getTodos();
      setTodos(data ?? []);
    } catch (e) {
      setError(e?.message ?? 'Failed to load todos');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    refreshTodos();
  }, []);

  async function onSubmit(e) {
    e.preventDefault();
    setError('');

    const trimmed = title.trim();
    if (!trimmed) {
      setError('Title is required');
      return;
    }

    try {
      if (formMode === 'create') {
        const created = await createTodo({ title: trimmed, completed });
        setTodos((prev) => [...prev, created]);
      } else {
        const updated = await updateTodo(editingId, { title: trimmed, completed });
        setTodos((prev) =>
          prev.map((t) => (t.id === updated.id ? updated : t)),
        );
      }

      setTitle('');
      setCompleted(false);
      setEditingId(null);
    } catch (e) {
      setError(e?.message ?? 'Request failed');
    }
  }

  function startEdit(todo) {
    setEditingId(todo.id);
    setTitle(todo.title);
    setCompleted(todo.completed);
    setError('');
  }

  async function onDelete(id) {
    if (!confirm('Delete this todo?')) return;
    setError('');
    try {
      await deleteTodo(id);
      setTodos((prev) => prev.filter((t) => t.id !== id));
      // If the user deletes the currently edited item
      if (editingId === id) {
        setEditingId(null);
        setTitle('');
        setCompleted(false);
      }
    } catch (e) {
      setError(e?.message ?? 'Delete failed');
    }
  }

  return (
    <div className="todoApp">
      <h1>Todo</h1>

      <form className="form" onSubmit={onSubmit}>
        <input
          type="text"
          value={title}
          placeholder="Enter title"
          onChange={(e) => setTitle(e.target.value)}
        />

        <label className="checkbox">
          <input
            type="checkbox"
            checked={completed}
            onChange={(e) => setCompleted(e.target.checked)}
          />
          Completed
        </label>

        <button type="submit" disabled={loading}>
          {formMode === 'create' ? 'Add' : 'Update'}
        </button>

        {formMode === 'edit' && (
          <button
            type="button"
            className="secondary"
            onClick={() => {
              setEditingId(null);
              setTitle('');
              setCompleted(false);
              setError('');
            }}
          >
            Cancel
          </button>
        )}
      </form>

      {loading && <p className="status">Loading...</p>}
      {error && (
        <p className="error" role="alert">
          {error}
        </p>
      )}

      <div className="list">
        {todos.length === 0 && !loading ? (
          <p className="status">No todos yet. Create your first one above.</p>
        ) : (
          todos.map((todo) => (
            <div className="item" key={todo.id}>
              <div className="left">
                <input
                  type="checkbox"
                  checked={todo.completed}
                  onChange={() =>
                    startEdit({
                      ...todo,
                      completed: !todo.completed,
                    })
                  }
                />
                <span className={todo.completed ? 'done' : ''}>
                  {todo.title}
                </span>
              </div>

              <div className="actions">
                <button type="button" onClick={() => startEdit(todo)}>
                  Edit
                </button>
                <button
                  type="button"
                  className="danger"
                  onClick={() => onDelete(todo.id)}
                >
                  Delete
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
