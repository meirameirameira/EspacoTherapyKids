// src/components/Patient/PacienteTable.jsx
import React from 'react';
import { TableContainer, Table, Th, Td } from '../common/Table';
import { pacienteColumns } from './pacienteColumns';
import { showOrDash } from '../../utils/format';

export default function PacienteTable({ pacientes }) {
  const rows = (pacientes && pacientes.length > 0)
    ? pacientes
    : Array.from({ length: 3 }, () => null);

  return (
    <TableContainer>
      <Table>
        <thead>
          <tr>
            {pacienteColumns.map(col => (
              <Th key={col.key}>{col.label}</Th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((p, rowIdx) => (
            <tr key={rowIdx}>
              {pacienteColumns.map(col => {
                // se for linha de exemplo, p === null
                if (!p) {
                  return <Td key={col.key}>—</Td>;
                }
                // calcula via computed de forma segura
                const raw = col.computed ? col.computed(p) : p[col.key];
                return (
                  <Td key={col.key}>
                    {showOrDash(raw, col.decimals)}
                  </Td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </Table>
    </TableContainer>
  );
}
