import React, { useState } from 'react';
import { deletePaciente } from '../utils/api';
import { Title, Form, Label, Input, Button } from './styled';

export default function DeletePatient() {
  const [id, setId] = useState('');
  const handleDelete = async e => {
    e.preventDefault();
    try {
      await deletePaciente(id);
      alert('Removido com sucesso');
    } catch {
      alert('Erro ao remover paciente');
    }
  };

  return (
    <>
      <Title>Remover Paciente</Title>
      <Form onSubmit={handleDelete}>
        <Label>ID:</Label>
        <Input value={id} onChange={e => setId(e.target.value)} />
        <Button type="submit">Remover</Button>
      </Form>
    </>
  );
}