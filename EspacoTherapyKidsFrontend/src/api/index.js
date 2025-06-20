const BASE = '/api/pacientes';

export async function fetchPacientes() {
  const res = await fetch(BASE);
  if (!res.ok) throw new Error('Erro ao buscar pacientes');
  return res.json();
}

export async function fetchPacienteById(id) {
  const res = await fetch(`${BASE}/${id}`);
  if (!res.ok) throw new Error('Paciente não encontrado');
  return res.json();
}

export async function createPaciente(data) {
  const res = await fetch(BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error('Erro ao criar paciente');
  return res.json();
}

export async function updatePaciente(id, data) {
  const res = await fetch(`${BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error('Erro ao atualizar paciente');
  return res.json();
}

export async function deletePaciente(id) {
  const resp = await fetch(`/api/pacientes/${id}`, {
    method: 'DELETE',
  });
  if (!resp.ok) {
    const texto = await resp.text();
    throw new Error(texto || 'Erro ao remover paciente');
  }
  return true;
}