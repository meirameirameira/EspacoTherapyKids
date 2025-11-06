import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/global.css';
import { logout as apiLogout } from '../api';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function Navbar({ onLogout }) {
  const [term, setTerm] = useState('');
  const navigate = useNavigate();

  function onSubmit(e) {
    e.preventDefault();
    const q = term.trim();
    if (!q) return;

    const onlyDigits = /^\d+$/.test(q);
    if (onlyDigits) {
      navigate(`/listar?by=id&q=${encodeURIComponent(q)}`);
    } else {
      navigate(`/listar?by=nome&q=${encodeURIComponent(q)}`);
    }
  }

  function handleLogout() {
    apiLogout();
    onLogout?.();
    navigate('/', { replace: true });
  }

  return (
    <nav className="navbar">
      <Link className="nav-left" to="/listar">
        <FontAwesomeIcon icon="fa-solid fa-house" /> Menu
      </Link>

      <form className="nav-search" onSubmit={onSubmit}>
        <input
          type="text"
          placeholder="Buscar por nome ou ID..."
          value={term}
          onChange={(e) => setTerm(e.target.value)}
          style={{ padding: '6px 8px', minWidth: 260, border: '1px solid #ccc', borderRadius: 4 }}
        />
        <button className="recarregar" type="submit">
          Buscar <FontAwesomeIcon icon="fa-solid fa-magnifying-glass" />
        </button>
      </form>

      <button className="nav-right" onClick={handleLogout}>
        Logout <FontAwesomeIcon icon="fa-solid fa-right-from-bracket" />
      </button>
    </nav>
  );
}
