const BASE = '/api';
export const login = async creds => {
  const res = await fetch(`${BASE}/login`, {
    method: 'POST', credentials: 'include', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(creds)
  });
  if (!res.ok) throw new Error('Falha no login');
  return await res.json();
};
export const fetchPacientes = async () => {
  const res = await fetch(`${BASE}/pacientes`);
  if (!res.ok) throw new Error('Erro ao listar pacientes');
  return await res.json();
};
export const fetchPacienteById = async id => {
  const res = await fetch(`${BASE}/pacientes/${id}`);
  if (!res.ok) throw new Error('Paciente não encontrado');
  return await res.json();
};
export const createPaciente = async data => {
  const res = await fetch(`${BASE}/pacientes`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
  });
  if (!res.ok) throw new Error('Erro ao criar paciente');
  return await res.json();
};
export const updatePaciente = async (id, data) => {
  const res = await fetch(`${BASE}/pacientes/${id}`, {
    method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
  });
  if (!res.ok) throw new Error('Erro ao atualizar paciente');
  return await res.json();
};
export const deletePaciente = async id => {
  const res = await fetch(`${BASE}/pacientes/${id}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Erro ao remover paciente');
};