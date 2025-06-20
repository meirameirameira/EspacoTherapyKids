import React from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';

const Nav = styled.nav`
  display: flex;
  gap: 8px;
  justify-content: center;
  margin: 16px 0;
`;

const LinkButton = styled(NavLink)`
  padding: 8px 12px;
  border-radius: 4px;
  text-decoration: none;
  background: #3498db;
  color: white;
  &.active {
    background: #2980b9;
  }
`;

export default function Navbar() {
  return (
    <Nav>
      <LinkButton to="/cadastrar">Cadastrar</LinkButton>
      <LinkButton to="/listar">Listar</LinkButton>
      <LinkButton to="/pesquisar">Pesquisar</LinkButton>
      <LinkButton to="/atualizar">Atualizar</LinkButton>
      <LinkButton to="/remover">Remover</LinkButton>
    </Nav>
  );
}
