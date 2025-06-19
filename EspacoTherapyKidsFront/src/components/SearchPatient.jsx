import React, { useState } from 'react';
import { fetchPacienteById } from '../utils/api';
import { Title, Form, Label, Input, Button } from './styled';

export default function SearchPatient() {
  const [id, setId] = useState('');
  const [paciente, setPaciente] = useState(null);

  const handleSearch = async e => {
    e.preventDefault();
    try {
      const data = await fetchPacienteById(id);
      setPaciente(data);
    } catch (e) {
      alert('Paciente não encontrado');
    }
  };

  return (
    <>
      <Title>Pesquisar Paciente</Title>
      <Form onSubmit={handleSearch}>
        <Label>ID:</Label>
        <Input value={id} onChange={e => setId(e.target.value)} />
        <Button type="submit">Buscar</Button>
      </Form>
      {paciente && <pre>{JSON.stringify(paciente, null, 2)}</pre>}
    </>
  );
}