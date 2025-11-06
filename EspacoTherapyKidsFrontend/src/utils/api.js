const BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

function authHeader() {
  const t = localStorage.getItem('token');
  return t ? { Authorization: `Bearer ${t}` } : {};
}

async function http(path, { method = 'GET', body, headers } = {}) {
  const res = await fetch(`${BASE}${path}`, {
    method,
    headers: { 'Content-Type': 'application/json', ...authHeader(), ...(headers || {}) },
    body: body ? JSON.stringify(body) : undefined,
  });

  const ct = res.headers.get('content-type') || '';
  const isJson = ct.includes('application/json');
  const data = isJson ? await res.json().catch(() => null) : null;

  if (!res.ok) {
    const err = new Error((data && data.message) || `HTTP ${res.status}`);
    err.status = res.status;
    err.body = data;
    throw err;
  }
  return data;
}

export async function login({ username, password }) {
  const data = await http('/auth/login', { method: 'POST', body: { username, password } });
  localStorage.setItem('token', data.accessToken);
  return data.accessToken;
}
export function logout() {
  localStorage.removeItem('token');
}

export async function fetchPacientes({ nome, nmResponsavel, page = 0, size = 10, sortBy = 'codigo', sortDir = 'asc' } = {}) {
  const qs = new URLSearchParams();
  if (nome) qs.set('nome', nome);
  if (nmResponsavel) qs.set('nmResponsavel', nmResponsavel);
  qs.set('page', page);
  qs.set('size', size);
  qs.set('sortBy', sortBy);
  qs.set('sortDir', sortDir);

  const pageResp = await http(`/pacientes?${qs.toString()}`);
  return pageResp?.content ?? pageResp ?? [];
}

export async function exportPacientesXlsx() {
  const res = await fetch(`${BASE}/pacientes/export`, {
    method: 'GET',
    headers: { ...authHeader() },
  });
  if (!res.ok) {
    const msg = `Falha ao exportar (HTTP ${res.status})`;
    throw new Error(msg);
  }
  const blob = await res.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'pacientes.xlsx';
  document.body.appendChild(a);
  a.click();
  a.remove();
  window.URL.revokeObjectURL(url);
}

export const fetchPacienteById  = (id)      => http(`/pacientes/${id}`);
export const createPaciente     = (payload) => http(`/pacientes`, { method: 'POST', body: payload });
export const updatePaciente     = (id, body)=> http(`/pacientes/${id}`, { method: 'PUT', body });
export const deletePaciente     = (id)      => http(`/pacientes/${id}`, { method: 'DELETE' });
