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
         const toNum = (v) => (v === '' || v === null || v === undefined ? undefined : Number(v));

         const paciente = {
           nome: vals.nome?.trim(),
           nmResponsavel: vals.nmResponsavel?.trim(),
           // garanta number (não string) para o backend
           nrResponsavel: Number(vals.nrResponsavel),

           // Fono: se desligado, mande valores neutros (sem quebrar validações/DAO)
           fono: vals.fonoEnabled
             ? {
                 preco: toNum(vals.fonoPreco),
                 horas: Math.max(1, Number(vals.fonoHoras)),           // inteiro >= 1
                 reembolsoInformado: toNum(vals.fonoReembolso),
               }
             : { preco: 0, horas: 0, reembolsoInformado: 1 },         // NUNCA 0 aqui

           // TO: mesma ideia
           terapiaOcupacional: vals.toEnabled
             ? {
                 preco: toNum(vals.toPreco),
                 horas: Math.max(1, Number(vals.toHoras)),
                 reembolsoInformado: toNum(vals.toReembolso),
               }
             : { preco: 0, horas: 0, reembolsoInformado: 1 },

           // ABA: no teu DAO, NF = ceil(preco / reembolso). Evite reembolso=0.
           // Também incluí 'horas' com 1 para compatibilidade de DTO, se existir.
           aba: vals.abaEnabled
             ? {
                 preco: toNum(vals.abaPreco),
                 horas: 1,
                 reembolsoInformado: toNum(vals.abaReembolso),
               }
             : { preco: 0, horas: 1, reembolsoInformado: 1 },
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
            <InputField name="nome" label="Nome" />
            <ErrorMessage name="nome" component="div" />

            <InputField name="nrResponsavel" label="Número do Responsável" type="text" />
            <ErrorMessage name="nrResponsavel" component="div" />
            <InputField name="nmResponsavel" label="Nome do Responsável" />
            <ErrorMessage name="nmResponsavel" component="div" />

            <Specializations>
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
