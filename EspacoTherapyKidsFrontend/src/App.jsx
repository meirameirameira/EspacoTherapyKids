import React, { useState } from 'react';
import LoginForm from './components/patient/LoginForm';
import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import CreatePatient from './components/patient/CreatePatient';
import ListPatients from './components/patient/ListPatients';
import SearchPatient from './components/patient/SearchPatient';
import UpdatePatient from './components/patient/UpdatePatient';
import DeletePatient from './components/patient/DeletePatient';
import "./styles/global.css";

export default function App() {

  
  const [loggedIn, setLoggedIn] = useState(false);

  function handleLoginSuccess() {
    setLoggedIn(true);
  }

  if (!loggedIn) {
    return <LoginForm onSuccess={handleLoginSuccess} />;
  }

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
