const BASE_URL = import.meta.env.VITE_API_BASE_URL || '';

export async function login({ username, password }) {
  const res = await fetch(`${BASE_URL}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  if (!res.ok) {
    throw new Error('Credenciais inválidas');
  }
  return await res.json();
}

export async function fetchPacientes() { /* ... */ }
export async function createPaciente(data) { /* ... */ }