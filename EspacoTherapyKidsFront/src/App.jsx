// src/App.jsx
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Container, Nav, LinkButton } from './components/styled';

import CreatePatient   from './components/CreatePatient';
import ListPatients    from './components/ListPatients';
import SearchPatient   from './components/SearchPatient';
import UpdatePatient   from './components/UpdatePatient';
import DeletePatient   from './components/DeletePatient';

export default function App() {
  return (
    <Container>
      <Nav>
        <LinkButton to="/cadastrar">Cadastrar</LinkButton>
        <LinkButton to="/listar">Listar</LinkButton>
        <LinkButton to="/pesquisar">Pesquisar</LinkButton>
        <LinkButton to="/atualizar">Atualizar</LinkButton>
        <LinkButton to="/remover">Remover</LinkButton>
      </Nav>

      <Routes>
        <Route path="/" element={<Navigate to="/listar" replace />} />
        <Route path="/cadastrar" element={<CreatePatient />} />
        <Route path="/listar"   element={<ListPatients />} />
        <Route path="/pesquisar" element={<SearchPatient />} />
        <Route path="/atualizar" element={<UpdatePatient />} />
        <Route path="/remover"   element={<DeletePatient />} />
        <Route path="*"         element={<Navigate to="/listar" replace />} />
      </Routes>
    </Container>
  );
}
