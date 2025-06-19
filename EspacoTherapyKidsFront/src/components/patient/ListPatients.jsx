import React, { useEffect, useState } from 'react';
import { fetchPacientes } from '../../api';
import PacienteTable from './PacienteTable';

export default function ListPatients() {
  const [pacientes, setPacientes] = useState([]);

  useEffect(() => {
    fetchPacientes()
      .then(data => setPacientes(data.sort((a, b) => a.codigo - b.codigo)))
      .catch(console.error);
  }, []);

  return (
    <>
      <h2 style={{ textAlign: 'center' }}>Listar Pacientes</h2>
      <PacienteTable pacientes={pacientes} />
    </>
  );
}
