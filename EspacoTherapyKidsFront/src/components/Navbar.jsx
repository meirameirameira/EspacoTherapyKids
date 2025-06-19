import React from 'react';
import { Nav, LinkButton, Button } from './styled';
export default function Navbar({ onLogout }) {
  return (
    <Nav>
      <LinkButton to="/cadastrar">Cadastrar</LinkButton>
      <LinkButton to="/listar">Listar</LinkButton>
      <LinkButton to="/pesquisar">Pesquisar</LinkButton>
      <LinkButton to="/atualizar">Atualizar</LinkButton>
      <LinkButton to="/remover">Remover</LinkButton>
      <Button onClick={onLogout}>Sair</Button>
    </Nav>
  );
}
