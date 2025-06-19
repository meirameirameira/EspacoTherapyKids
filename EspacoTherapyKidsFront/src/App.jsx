import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import CreatePatient from './components/Patient/CreatePatient';
import ListPatients from './components/Patient/ListPatients';
import SearchPatient from './components/Patient/SearchPatient';
import UpdatePatient from './components/Patient/UpdatePatient';
import DeletePatient from './components/Patient/DeletePatient';

export default function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/listar" replace />} />
        <Route path="/cadastrar" element={<CreatePatient />} />
        <Route path="/listar"   element={<ListPatients />} />
        <Route path="/pesquisar" element={<SearchPatient />} />
        <Route path="/atualizar" element={<UpdatePatient />} />
        <Route path="/remover"   element={<DeletePatient />} />
        <Route path="*" element={<Navigate to="/listar" replace />} />
      </Routes>
    </>
  );
}
