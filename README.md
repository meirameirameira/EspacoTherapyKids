# Espaço Therapy Kids Store

**Gerenciamento de pacientes e sessões terapêuticas (Fonoaudiologia, Terapia Ocupacional e ABA)**

**Descrição do repositório:**

Este projeto é uma aplicação Java de console que permite a gestão completa de pacientes e suas sessões terapêuticas em clínicas pediátricas. Com arquitetura MVC/DAO e integração Oracle/JDBC, oferece operações CRUD para pacientes, cálculo automático de custos, reembolsos e emissão de nota fiscal.

## Recursos

* Cadastro, listagem, pesquisa, atualização e remoção de pacientes.
* Três modalidades de sessão: Fonoaudiologia, Terapia Ocupacional e ABA.
* Cálculo automático de:

  * **Fonoaudiologia**
  * **Terapia Ocupacional**
  * **ABA**
* Persistência em **Oracle** via **JDBC**.
* Arquitetura MVC separando modelo, acesso a dados (DAO) e apresentação (views).

## Tecnologias

* Java JDK 11 ou superior
* Oracle Database (ex.: XE)
* Biblioteca JDBC do Oracle no classpath

## Uso

Cada view possui um método `main` no pacote `org.example.view`:

* **PacienteView**: menu principal
* **CadastroPacienteView**: cadastrar novo paciente
* **ListagemPacienteView**: listar todos
* **PesquisarPacientePorIdView**: buscar por ID
* **AtualizacaoPacienteView**: atualizar dados
* **RemoverPacienteView**: excluir paciente

## Estrutura do Projeto

```
src/
 └─ org.example
     ├─ factory
     │   └─ ConnectionFactory.java
     ├─ exception
     │   └─ EntidadeNaoEncontradaException.java
     ├─ model
     │   ├─ Paciente.java
     │   └─ Sessao.java
     ├─ dao
     │   └─ PacienteDao.java
     └─ view
         ├─ PacienteView.java
         ├─ CadastroPacienteView.java
         ├─ ListagemPacienteView.java
         ├─ PesquisarPacientePorIdView.java
         ├─ AtualizacaoPacienteView.java
         └─ RemoverPacienteView.java
```
