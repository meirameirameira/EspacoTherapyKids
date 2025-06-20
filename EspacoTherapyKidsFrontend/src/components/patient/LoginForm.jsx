import React from 'react';
import { Formik, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { login } from '../../utils/api';
import { Title, Form, Label, Input, Button } from '../styled';

const schema = Yup.object({ username: Yup.string().required(), password: Yup.string().required() });
export default function LoginForm({ onSuccess }) {
  return (
    <>
      <Title>Login</Title>
      <Formik initialValues={{ username: '', password: '' }} validationSchema={schema}
        onSubmit={async (values, { setSubmitting, setErrors }) => {
          try {
            await login(values);
            onSuccess();
          } catch { setErrors({ password: 'Credenciais inválidas' }); }
          setSubmitting(false);
        }}>
        {({ handleSubmit }) => (
          <Form onSubmit={handleSubmit}>
            <Label>Usuário:</Label>
            <Field as={Input} name="username" />
            <ErrorMessage name="username" component="div" />
            <Label>Senha:</Label>
            <Field type="password" as={Input} name="password" />
            <ErrorMessage name="password" component="div" />
            <Button type="submit">Entrar</Button>
          </Form>
        )}
      </Formik>
    </>
  );
}