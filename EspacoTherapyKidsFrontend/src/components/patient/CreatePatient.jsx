// src/components/Patient/CreatePatient.jsx
import React from 'react';
import { Formik, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import styled from 'styled-components';
import FormWrapper from '../common/FormWrapper';
import InputField from '../common/InputField';
import Button from '../common/Button';
import { createPaciente } from '../../api';

const Specializations = styled.div`
  display: flex;
  gap: 20px;
  margin-top: 16px;
  justify-content: center;
`;

// Cada caixa de especialização com largura fixa de 300px
const SpecSection = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  border: 1px solid #ddd;
  padding: 12px;
  border-radius: 4px;
  flex: 0 1 300px;
  min-width: 300px;
  max-width: 300px;
`;

// Validação do formulário
const schema = Yup.object({
  nome: Yup.string().required('Obrigatório'),
  nrResponsavel: Yup.string().required('Obrigatório'),
  nmResponsavel: Yup.string().required('Obrigatório'),
  fonoEnabled: Yup.boolean(),
  fonoPreco: Yup.number().when('fonoEnabled', {
    is: true, then: s => s.positive().required('Obrigatório'), otherwise: s => s.notRequired()
  }),
  fonoHoras: Yup.number().when('fonoEnabled', {
    is: true, then: s => s.integer().positive().required('Obrigatório'), otherwise: s => s.notRequired()
  }),
  fonoReembolso: Yup.number().when('fonoEnabled', {
    is: true, then: s => s.positive().required('Obrigatório'), otherwise: s => s.notRequired()
  }),
  toEnabled: Yup.boolean(),
  toPreco: Yup.number().when('toEnabled', {
    is: true, then: s => s.positive().required('Obrigatório'), otherwise: s => s.notRequired()
  }),
  toHoras: Yup.number().when('toEnabled', {
    is: true, then: s => s.integer().positive().required('Obrigatório'), otherwise: s => s.notRequired()
  }),
  toReembolso: Yup.number().when('toEnabled', {
    is: true, then: s => s.positive().required('Obrigatório'), otherwise: s => s.notRequired()
  }),
  abaEnabled: Yup.boolean(),
  abaPreco: Yup.number().when('abaEnabled', {
    is: true, then: s => s.positive().required('Obrigatório'), otherwise: s => s.notRequired()
  }),
  abaReembolso: Yup.number().when('abaEnabled', {
    is: true, then: s => s.positive().required('Obrigatório'), otherwise: s => s.notRequired()
  }),
});

export default function CreatePatient() {
  return (
    <>
      <h2 style={{ textAlign: 'center' }}>Cadastrar Paciente</h2>
      <Formik
        initialValues={{
          nome: '',
          nrResponsavel: '',
          nmResponsavel: '',
          fonoEnabled: false,
          fonoPreco: '',
          fonoHoras: '',
          fonoReembolso: '',
          toEnabled: false,
          toPreco: '',
          toHoras: '',
          toReembolso: '',
          abaEnabled: false,
          abaPreco: '',
          abaReembolso: '',
        }}
        validationSchema={schema}
        onSubmit={async (vals, { resetForm }) => {
          const paciente = {
            nome: vals.nome,
            nrResponsavel: vals.nrResponsavel,
            nmResponsavel: vals.nmResponsavel,
            fono: vals.fonoEnabled
              ? {
                  preco: parseFloat(vals.fonoPreco),
                  horas: parseInt(vals.fonoHoras, 10),
                  reembolsoInformado: parseFloat(vals.fonoReembolso),
                }
              : { preco: 0, horas: 0, reembolsoInformado: 0 },
            terapiaOcupacional: vals.toEnabled
              ? {
                  preco: parseFloat(vals.toPreco),
                  horas: parseInt(vals.toHoras, 10),
                  reembolsoInformado: parseFloat(vals.toReembolso),
                }
              : { preco: 0, horas: 0, reembolsoInformado: 0 },
            aba: vals.abaEnabled
              ? {
                  preco: parseFloat(vals.abaPreco),
                  reembolsoInformado: parseFloat(vals.abaReembolso),
                }
              : { preco: 0, reembolsoInformado: 0 },
          };
          try {
            await createPaciente(paciente);
            alert('Paciente cadastrado com sucesso!');
            resetForm();
          } catch (error) {
            console.error('Erro ao criar paciente', error);
            alert('Falha ao cadastrar paciente: ' + error.message);
          }
        }}
      >
        {({ values, handleSubmit, isSubmitting, setFieldValue }) => (
          <FormWrapper onSubmit={handleSubmit}>
            {/* Campo Nome e Responsável */}
            <InputField name="nome" label="Nome" />
            <ErrorMessage name="nome" component="div" />

            <InputField name="nrResponsavel" label="Número do Responsável" type="text" />
            <ErrorMessage name="nrResponsavel" component="div" />
            <InputField name="nmResponsavel" label="Nome do Responsável" />
            <ErrorMessage name="nmResponsavel" component="div" />

            {/* Especializações lado a lado */}
            <Specializations>
              {/* Fonoaudiologia */}
              <SpecSection>
                <label>
                  <Field
                    type="checkbox"
                    name="fonoEnabled"
                    checked={values.fonoEnabled}
                    onChange={() => setFieldValue('fonoEnabled', !values.fonoEnabled)}
                  />{' '}
                  Fonoaudiologia
                </label>
                <InputField name="fonoPreco" label="Valor sessão (R$)" type="number" disabled={!values.fonoEnabled} />
                <ErrorMessage name="fonoPreco" component="div" />
                <InputField name="fonoHoras" label="Horas de sessão" type="number" disabled={!values.fonoEnabled} />
                <ErrorMessage name="fonoHoras" component="div" />
                <InputField name="fonoReembolso" label="Reembolso informado" type="number" disabled={!values.fonoEnabled} />
                <ErrorMessage name="fonoReembolso" component="div" />
              </SpecSection>

              {/* Terapia Ocupacional */}
              <SpecSection>
                <label>
                  <Field
                    type="checkbox"
                    name="toEnabled"
                    checked={values.toEnabled}
                    onChange={() => setFieldValue('toEnabled', !values.toEnabled)}
                  />{' '}
                  Terapia Ocupacional
                </label>
                <InputField name="toPreco" label="Valor sessão (R$)" type="number" disabled={!values.toEnabled} />
                <ErrorMessage name="toPreco" component="div" />
                <InputField name="toHoras" label="Horas de sessão" type="number" disabled={!values.toEnabled} />
                <ErrorMessage name="toHoras" component="div" />
                <InputField name="toReembolso" label="Reembolso informado" type="number" disabled={!values.toEnabled} />
                <ErrorMessage name="toReembolso" component="div" />
              </SpecSection>

              {/* Pacote ABA */}
              <SpecSection>
                <label>
                  <Field
                    type="checkbox"
                    name="abaEnabled"
                    checked={values.abaEnabled}
                    onChange={() => setFieldValue('abaEnabled', !values.abaEnabled)}
                  />{' '}
                  Terapia ABA
                </label>
                <InputField name="abaPreco" label="Valor do pacote (R$)" type="number" disabled={!values.abaEnabled} />
                <ErrorMessage name="abaPreco" component="div" />
                <InputField name="abaReembolso" label="Reembolso informado" type="number" disabled={!values.abaEnabled} />
                <ErrorMessage name="abaReembolso" component="div" />
              </SpecSection>
            </Specializations>

            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Enviando...' : 'Cadastrar'}
            </Button>
          </FormWrapper>
        )}
      </Formik>
    </>
  );
}
