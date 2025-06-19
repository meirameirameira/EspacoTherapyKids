// src/components/CreatePatient.jsx
import React from 'react';
import { Formik, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { Title, Form, Label, Input, Button } from './styled';
import { createPaciente } from '../utils/api';

const validationSchema = Yup.object({
  nome: Yup.string().required('Obrigatório'),
  fonoPreco: Yup.number()
    .typeError('Deve ser número')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  fonoHoras: Yup.number()
    .typeError('Deve ser número inteiro')
    .integer('Deve ser inteiro')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  fonoReembolso: Yup.number()
    .typeError('Deve ser número')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  toPreco: Yup.number()
    .typeError('Deve ser número')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  toHoras: Yup.number()
    .typeError('Deve ser número inteiro')
    .integer('Deve ser inteiro')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  toReembolso: Yup.number()
    .typeError('Deve ser número')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  abaPreco: Yup.number()
    .typeError('Deve ser número')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  abaReembolso: Yup.number()
    .typeError('Deve ser número')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
});

export default function CreatePatient({ onSuccess }) {
  const initialValues = {
    nome: '',
    fonoPreco: '',
    fonoHoras: '',
    fonoReembolso: '',
    toPreco: '',
    toHoras: '',
    toReembolso: '',
    abaPreco: '',
    abaReembolso: '',
  };

  const handleSubmit = async (values, { setSubmitting, resetForm }) => {
    try {
      // Ajusta o payload para bater com seu backend Java
      const payload = {
        nome: values.nome,
        fono: {
          preco: parseFloat(values.fonoPreco),
          horas: parseInt(values.fonoHoras, 10),
          reembolsoInformado: parseFloat(values.fonoReembolso),
        },
        terapiaOcupacional: {
          preco: parseFloat(values.toPreco),
          horas: parseInt(values.toHoras, 10),
          reembolsoInformado: parseFloat(values.toReembolso),
        },
        aba: {
          preco: parseFloat(values.abaPreco),
          // para ABA usamos o construtor que assume 1 hora
          reembolsoInformado: parseFloat(values.abaReembolso),
        },
      };
      await createPaciente(payload);
      alert('Paciente cadastrado com sucesso!');
      resetForm();
      onSuccess?.();
    } catch (err) {
      console.error(err);
      alert('Erro ao cadastrar paciente');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <Title>Cadastrar Paciente</Title>
      <Formik
        initialValues={initialValues}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        {({ handleSubmit, isSubmitting }) => (
          <Form onSubmit={handleSubmit}>
            <Label>Nome:</Label>
            <Field name="nome" as={Input} />
            <ErrorMessage name="nome" component="div" />

            <Title as="h3">Fonoaudiologia</Title>
            <Label>Valor de sessão (R$):</Label>
            <Field name="fonoPreco" as={Input} />
            <ErrorMessage name="fonoPreco" component="div" />

            <Label>Horas de sessão:</Label>
            <Field name="fonoHoras" as={Input} />
            <ErrorMessage name="fonoHoras" component="div" />

            <Label>Reembolso informado (R$):</Label>
            <Field name="fonoReembolso" as={Input} />
            <ErrorMessage name="fonoReembolso" component="div" />

            <Title as="h3">Terapia Ocupacional</Title>
            <Label>Valor de sessão (R$):</Label>
            <Field name="toPreco" as={Input} />
            <ErrorMessage name="toPreco" component="div" />

            <Label>Horas de sessão:</Label>
            <Field name="toHoras" as={Input} />
            <ErrorMessage name="toHoras" component="div" />

            <Label>Reembolso informado (R$):</Label>
            <Field name="toReembolso" as={Input} />
            <ErrorMessage name="toReembolso" component="div" />

            <Title as="h3">Terapia ABA</Title>
            <Label>Valor do pacote (R$):</Label>
            <Field name="abaPreco" as={Input} />
            <ErrorMessage name="abaPreco" component="div" />

            <Label>Reembolso informado (R$):</Label>
            <Field name="abaReembolso" as={Input} />
            <ErrorMessage name="abaReembolso" component="div" />

            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Enviando...' : 'Cadastrar'}
            </Button>
          </Form>
        )}
      </Formik>
    </>
  );
}
