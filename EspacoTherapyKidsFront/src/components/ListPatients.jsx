// src/components/ListPatients.jsx
import React, { useEffect, useState } from 'react';
import { fetchPacientes } from '../utils/api';
import { Container, Title, TableContainer, Table, Th, Td } from './styled';

export default function ListPatients() {
  const [pacientes, setPacientes] = useState([]);

  useEffect(() => {
    fetchPacientes()
      .then(setPacientes)
      .catch(console.error);
  }, []);

  const renderHeader = () => (
    <thead>
      <tr>
        <Th>CD_PACIENTE</Th>
        <Th>NM_PACIENTE</Th>
        <Th>PRECO_FONO</Th>
        <Th>HORAS_FONO</Th>
        <Th>TOTAL_FONO</Th>
        <Th>REEMBOLSO_FONO</Th>
        <Th>NF_FONO</Th>
        <Th>PRECO_TO</Th>
        <Th>HORAS_TO</Th>
        <Th>TOTAL_TO</Th>
        <Th>REEMBOLSO_TO</Th>
        <Th>NF_TO</Th>
        <Th>PRECO_ABA</Th>
        <Th>REEMBOLSO_ABA</Th>
        <Th>NF_ABA</Th>
      </tr>
    </thead>
  );

  // Gera N linhas de exemplo com 15 colunas cada
  const renderExampleRows = (count = 3) => (
    <tbody>
      {Array.from({ length: count }).map((_, rowIndex) => (
        <tr key={rowIndex}>
          {Array.from({ length: 15 }).map((_, colIndex) => (
            <Td key={colIndex}>—</Td>
          ))}
        </tr>
      ))}
    </tbody>
  );

  const renderDataRows = () => (
    <tbody>
      {pacientes.map(p => (
        <tr key={p.codigo}>
          <Td>{p.codigo}</Td>
          <Td>{p.nome}</Td>
          <Td>{p.fono.preco.toFixed(2)}</Td>
          <Td>{p.fono.horas}</Td>
          <Td>{(p.fono.preco * p.fono.horas).toFixed(2)}</Td>
          <Td>{p.fono.reembolsoInformado.toFixed(2)}</Td>
          <Td>{p.fono.notasFiscais}</Td>
          <Td>{p.terapiaOcupacional.preco.toFixed(2)}</Td>
          <Td>{p.terapiaOcupacional.horas}</Td>
          <Td>{(p.terapiaOcupacional.preco * p.terapiaOcupacional.horas).toFixed(2)}</Td>
          <Td>{p.terapiaOcupacional.reembolsoInformado.toFixed(2)}</Td>
          <Td>{p.terapiaOcupacional.notasFiscais}</Td>
          <Td>{p.aba.preco.toFixed(2)}</Td>
          <Td>{p.aba.reembolsoInformado.toFixed(2)}</Td>
          <Td>{p.aba.notasFiscais}</Td>
        </tr>
      ))}
    </tbody>
  );

  return (
    <Container>
      <Title>Listar Pacientes</Title>
      <TableContainer>
        <Table>
          {renderHeader()}
          {pacientes.length === 0
            ? renderExampleRows(3)   /* exibe 3 linhas de exemplo */
            : renderDataRows()}
        </Table>
      </TableContainer>
    </Container>
  );
}
