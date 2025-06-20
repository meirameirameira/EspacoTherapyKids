import React, { useState } from 'react';
import { fetchPacienteById, updatePaciente } from '../../api';
import { Formik, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import styled from 'styled-components';
import SearchById from '../common/SearchById';
import InputField from '../common/InputField';
import Button from '../common/Button';
import FormWrapper from '../common/FormWrapper';
import PacienteTable from './PacienteTable';

const Specializations = styled.div`
  display: flex;
  gap: 20px;
  margin: 24px 0;
`;
const SpecSection = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  border: 1px solid #ddd;
  padding: 12px;
  border-radius: 4px;
  flex: 0 1 300px;
`;

const schema = Yup.object({
  nome: Yup.string().required('Obrigatório'),
  fonoEnabled: Yup.boolean(),
  fonoPreco: Yup.number().when('fonoEnabled', { is: true, then: s => s.positive().required('Obrigatório') }),
  fonoHoras: Yup.number().when('fonoEnabled', { is: true, then: s => s.integer().positive().required('Obrigatório') }),
  fonoReembolso: Yup.number().when('fonoEnabled', { is: true, then: s => s.positive().required('Obrigatório') }),
  toEnabled: Yup.boolean(),
  toPreco: Yup.number().when('toEnabled', { is: true, then: s => s.positive().required('Obrigatório') }),
  toHoras: Yup.number().when('toEnabled', { is: true, then: s => s.integer().positive().required('Obrigatório') }),
  toReembolso: Yup.number().when('toEnabled', { is: true, then: s => s.positive().required('Obrigatório') }),
  abaEnabled: Yup.boolean(),
  abaPreco: Yup.number().when('abaEnabled', { is: true, then: s => s.positive().required('Obrigatório') }),
  abaReembolso: Yup.number().when('abaEnabled', { is: true, then: s => s.positive().required('Obrigatório') }),
});

export default function UpdatePatient() {
  const [id, setId] = useState('');
  const [paciente, setPaciente] = useState(null);

  const handleSearch = async e => {
    e.preventDefault();
    try {
      const data = await fetchPacienteById(id);
      setPaciente(data);
    } catch {
      alert('Paciente não encontrado');
      setPaciente(null);
    }
  };

  return (
    <>
      <h2 style={{ textAlign: 'center' }}>Atualizar Paciente</h2>
      <SearchById
        id={id}
        onChangeId={setId}
        onSearch={handleSearch}
        label="ID do Paciente"
        buttonText="Buscar"
      />

      {paciente && <PacienteTable pacientes={[paciente]} />}

      {paciente && (
        <Formik
          initialValues={{
            nome: paciente.nome,
            fonoEnabled: paciente.fono.horas > 0 || paciente.fono.preco > 0,
            fonoPreco: paciente.fono.preco,
            fonoHoras: paciente.fono.horas,
            fonoReembolso: paciente.fono.reembolsoInformado,
            toEnabled: paciente.terapiaOcupacional.horas > 0 || paciente.terapiaOcupacional.preco > 0,
            toPreco: paciente.terapiaOcupacional.preco,
            toHoras: paciente.terapiaOcupacional.horas,
            toReembolso: paciente.terapiaOcupacional.reembolsoInformado,
            abaEnabled: paciente.aba.preco > 0 || paciente.aba.reembolsoInformado > 0,
            abaPreco: paciente.aba.preco,
            abaReembolso: paciente.aba.reembolsoInformado,
          }}
          validationSchema={schema}
          onSubmit={async vals => {
            await updatePaciente(paciente.codigo, {
              nome: vals.nome,
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
            });
            alert('Atualizado com sucesso');
          }}
        >
          {({ values, handleSubmit, isSubmitting, setFieldValue }) => (
            <FormWrapper onSubmit={handleSubmit}>
              <InputField name="nome" label="Nome" />

              <Specializations>
                <SpecSection>
                  <label>
                    <Field
                      type="checkbox"
                      name="fonoEnabled"
                      checked={values.fonoEnabled}
                      onChange={() =>
                        setFieldValue('fonoEnabled', !values.fonoEnabled)
                      }
                    />{' '}
                    Fonoaudiologia
                  </label>
                  <InputField
                    name="fonoPreco"
                    label="Valor sessão (R$)"
                    type="number"
                    disabled={!values.fonoEnabled}
                  />
                  <ErrorMessage name="fonoPreco" component="div" />
                  <InputField
                    name="fonoHoras"
                    label="Horas de sessão"
                    type="number"
                    disabled={!values.fonoEnabled}
                  />
                  <ErrorMessage name="fonoHoras" component="div" />
                  <InputField
                    name="fonoReembolso"
                    label="Reembolso informado"
                    type="number"
                    disabled={!values.fonoEnabled}
                  />
                  <ErrorMessage name="fonoReembolso" component="div" />
                </SpecSection>

                <SpecSection>
                  <label>
                    <Field
                      type="checkbox"
                      name="toEnabled"
                      checked={values.toEnabled}
                      onChange={() =>
                        setFieldValue('toEnabled', !values.toEnabled)
                      }
                    />{' '}
                    Terapia Ocupacional
                  </label>
                  <InputField
                    name="toPreco"
                    label="Valor sessão (R$)"
                    type="number"
                    disabled={!values.toEnabled}
                  />
                  <ErrorMessage name="toPreco" component="div" />
                  <InputField
                    name="toHoras"
                    label="Horas de sessão"
                    type="number"
                    disabled={!values.toEnabled}
                  />
                  <ErrorMessage name="toHoras" component="div" />
                  <InputField
                    name="toReembolso"
                    label="Reembolso informado"
                    type="number"
                    disabled={!values.toEnabled}
                  />
                  <ErrorMessage name="toReembolso" component="div" />
                </SpecSection>

                <SpecSection>
                  <label>
                    <Field
                      type="checkbox"
                      name="abaEnabled"
                      checked={values.abaEnabled}
                      onChange={() =>
                        setFieldValue('abaEnabled', !values.abaEnabled)
                      }
                    />{' '}
                    Terapia ABA
                  </label>
                  <InputField
                    name="abaPreco"
                    label="Valor do pacote (R$)"
                    type="number"
                    disabled={!values.abaEnabled}
                  />
                  <ErrorMessage name="abaPreco" component="div" />
                  <InputField
                    name="abaReembolso"
                    label="Reembolso informado"
                    type="number"
                    disabled={!values.abaEnabled}
                  />
                  <ErrorMessage name="abaReembolso" component="div" />
                </SpecSection>
              </Specializations>

              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Atualizando...' : 'Atualizar'}
              </Button>
            </FormWrapper>
          )}
        </Formik>
      )}
    </>
  );
}
