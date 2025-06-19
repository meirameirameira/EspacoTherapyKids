// src/components/styled.js
import styled from 'styled-components';
import { NavLink } from 'react-router-dom';

/* Se quiser que tudo ocupe 100% da janela, tire o max-width */
export const Container = styled.div`
  /* max-width: 1200px; */
  width: 98%;
  margin: 0 auto;
  padding: 16px;
`;

export const Title = styled.h2`
  text-align: center;
  margin-bottom: 24px;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  max-width: 400px;
  width: 100%;
  margin: 0 auto 24px;
  gap: 12px;
  align-items: center;
`;

export const Label = styled.label`
  font-weight: bold;
  width: 100%;
`;

export const Input = styled.input`
  padding: 8px;
  font-size: 1rem;
  width: 100%;
  box-sizing: border-box;
`;

export const Button = styled.button`
  padding: 10px 16px;
  font-size: 1rem;
  cursor: pointer;
`;

export const Nav = styled.nav`
  display: flex;
  gap: 8px;
  justify-content: center;
  margin-bottom: 20px;
`;

export const LinkButton = styled(NavLink)`
  padding: 8px 12px;
  border-radius: 4px;
  text-decoration: none;
  background: #3498db;
  color: white;
  &.active {
    background: #2980b9;
  }
`;

/* Container de tabela ocupa 100% e permite scroll horizontal */
export const TableContainer = styled.div`
  width: 100%;
  overflow-x: auto;
  margin-bottom: 20px;
`;

/* Tabela sempre 100% do container */
export const Table = styled.table`
  border-collapse: collapse;
  width: 100% !important;
  table-layout: auto;
`;

export const Th = styled.th`
  border: 1px solid #ccc;
  padding: 8px;
  background: #f5f5f5;
  text-align: center;
  word-break: break-word;
`;

export const Td = styled.td`
  border: 1px solid #ccc;
  padding: 8px;
  text-align: center;
  word-break: break-word;
`;
