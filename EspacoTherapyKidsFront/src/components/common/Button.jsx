// src/components/common/Button.jsx
import styled from 'styled-components';

const Button = styled.button`
  width: 100%;
  padding: 12px;
  font-size: 1rem;
  color: #fff;
  background-color: ${props =>
    props.variant === 'danger' ? '#e74c3c' : '#3498db'};
  border: none;
  border-radius: 4px;
  cursor: pointer;
  box-sizing: border-box;

  &:hover {
    background-color: ${props =>
      props.variant === 'danger' ? '#c0392b' : '#2980b9'};
  }
`;

export default Button;
