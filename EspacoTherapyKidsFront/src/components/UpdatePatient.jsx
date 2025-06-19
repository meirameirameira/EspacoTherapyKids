import React from 'react';
import { Formik, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { Title, Form, Label, Input, Button } from './styled';
import { updatePaciente } from '../utils/api';

const validationSchema = Yup.object({
  id: Yup.number()
    .typeError('Deve ser um número')
    .integer('Deve ser inteiro')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  nome: Yup.string().required('Obrigatório'),
  fonoPreco: Yup.number()
    .typeError('Deve ser número')
    .positive('Deve ser positivo')
    .required('Obrigatório'),
  fonoHoras: Yup.number()
    .typeError('Deve ser inteiro')
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
    .typeError('Deve ser inteiro')
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

export default function UpdatePatient() {
  const initialValues = {
    id: '',
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
          reembolsoInformado: parseFloat(values.abaReembolso),
        },
      };
      await updatePaciente(values.id, payload);
      alert('Paciente atualizado com sucesso!');
      resetForm();
    } catch (err) {
      console.error(err);
      alert('Erro ao atualizar paciente');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <Title>Atualizar Paciente</Title>
      <Formik
        initialValues={initialValues}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        {({ handleSubmit, isSubmitting }) => (
          <Form onSubmit={handleSubmit}>
            <Label>ID:</Label>
            <Field name="id" as={Input} placeholder="Digite o ID" />
            <ErrorMessage name="id" component="div" />

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

            <Title as="h3">Pacote ABA</Title>
            <Label>Valor do pacote (R$):</Label>
            <Field name="abaPreco" as={Input} />
            <ErrorMessage name="abaPreco" component="div" />

            <Label>Reembolso informado (R$):</Label>
            <Field name="abaReembolso" as={Input} />
            <ErrorMessage name="abaReembolso" component="div" />

            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Atualizando...' : 'Atualizar'}
            </Button>
          </Form>
        )}
      </Formik>
    </>
  );
}