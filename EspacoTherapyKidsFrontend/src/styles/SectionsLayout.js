import styled from 'styled-components';

export const Page = styled.div`
  width: 100%;
  padding: 16px 12px;
`;

export const LargeForm = styled.form`
  max-width: 1100px;   /* controla o “tamanho do palco” */
  margin: 0 auto;      /* centraliza na página */
`;

export const FormInner = styled.div`
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
`;

export const Specializations = styled.div`
  /* palco das caixas */
  max-width: 1100px;
  margin: 12px auto 16px;

  /* grid responsivo: 1–3 colunas */
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(3, 1fr);

  @media (max-width: 992px) {
    grid-template-columns: repeat(2, 1fr);
  }
  @media (max-width: 640px) {
    grid-template-columns: 1fr;
  }
`;

export const SpecSection = styled.div`
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 12px;
  background: #fff;

  /* NÃO fixe width aqui — deixa o grid mandar */
  /* no máximo, limite exageros */
  min-width: 0; 
`;

export const FullWidthActions = styled.div`
  max-width: 1100px;
  margin: 12px auto 0;
  display: flex;
  justify-content: center;
  gap: 12px;
`;
