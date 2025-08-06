import React, { useState } from 'react';
import { Formik, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { login } from '../../api';
import { Title, Form, Label, Input, Button } from '../common/styled';

const schema = Yup.object({
  username: Yup.string().required('Obrigatório'),
  password: Yup.string().required('Obrigatório')
});

export default function LoginForm({ onSuccess }) {
  const [attempts, setAttempts] = useState(0);
  const maxAttempts = 3;

  return (
    <>
      <Title>Login</Title>
      {attempts >= maxAttempts ? (
        <p style={{ textAlign: 'center', color: 'red' }}>
          Você excedeu o número máximo de tentativas. Tente novamente mais tarde.
        </p>
      ) : (
        <Formik
          initialValues={{ username: '', password: '' }}
          validationSchema={schema}
          onSubmit={async (values, { setSubmitting, setErrors }) => {
            try {
              await login(values);
              onSuccess();
            } catch {
              const newAttempts = attempts + 1;
              setAttempts(newAttempts);
              if (newAttempts >= maxAttempts) {
                setErrors({ password: 'Número máximo de tentativas excedido' });
              } else {
                setErrors({ password: `Credenciais inválidas. Tentativas restantes: ${maxAttempts - newAttempts}` });
              }
            } finally {
              setSubmitting(false);
            }
          }}
        >
          {({ handleSubmit, isSubmitting }) => (
            <Form onSubmit={handleSubmit}>
              <Label>Usuário:</Label>
              <Field as={Input} name="username" disabled={isSubmitting} />
              <ErrorMessage name="username" component="div" />

              <Label>Senha:</Label>
              <Field type="password" as={Input} name="password" disabled={isSubmitting} />
              <ErrorMessage name="password" component="div" />

              <Button type="submit" disabled={isSubmitting}>
                Entrar
              </Button>
            </Form>
          )}
        </Formik>
      )}
    </>
  );
}
