const BASE = '/api/pacientes';

/**
 * @param {{username: string, password: string}} creds
 * @returns {Promise<string>}
 */
export async function login({ username, password }) {

  const res = await fetch('/api/login', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  console.log('[API] status', res.status);

  const text = await res.text();
  console.log('[API] body:', text);

  if (!res.ok) {
    throw new Error(text || 'Credenciais inválidas');
  }
  return text;
}

export async function fetchPacientes() {
  const res = await fetch(BASE, {
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Erro ao buscar pacientes');
  return res.json();
}

export async function fetchPacienteById(id) {
  const res = await fetch(`${BASE}/${id}`, {
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Paciente não encontrado');
  return res.json();
}

export async function createPaciente(data) {
  const res = await fetch(BASE, {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error('Erro ao criar paciente');
  return res.json();
}

export async function updatePaciente(id, data) {
  const res = await fetch(`${BASE}/${id}`, {
    method: 'PUT',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error('Erro ao atualizar paciente');
  return res.json();
}

export async function deletePaciente(id) {
  const res = await fetch(`${BASE}/${id}`, {
    method: 'DELETE',
    credentials: 'include'
  });
  if (!res.ok) {
    const texto = await res.text();
    throw new Error(texto || 'Erro ao remover paciente');
  }
  return true;
}
