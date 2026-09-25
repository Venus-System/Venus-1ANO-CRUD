# Venus CRUD - 1º Ano

Sistema web desenvolvido pela turma do 1º ano do Venus para a disciplina de 
Programação Orientada a Objetos, com landing page pública e área administrativa 
protegida para gerenciamento de dados via CRUD.

## 📋 Sobre o projeto

O sistema é composto por duas partes:

- **Landing page pública**: página de apresentação, acessível a qualquer visitante.
- **Área secreta (administrativa)**: acessível apenas mediante autenticação, onde 
  é possível realizar operações de Cadastro, Leitura, Atualização e Exclusão (CRUD) 
  em tabelas de um banco de dados PostgreSQL.

## 🛠️ Tecnologias utilizadas

- **Java** — linguagem principal do backend
- **Servlet** — recebimento e resposta de requisições HTTP
- **JDBC** — comunicação com o banco de dados
- **PostgreSQL** — banco de dados relacional
- **HTML / CSS** — interface web

## 📁 Estrutura do projeto

O código é organizado separando responsabilidades em pacotes:

- `servlet/` — camada responsável por receber requisições HTTP e devolver respostas
- `dao/` — camada de acesso a dados (Data Access Object), responsável pela comunicação com o PostgreSQL via JDBC
- `model/` — classes que representam as entidades do banco de dados

## ✅ Funcionalidades

- CRUD completo em pelo menos três tabelas do banco de dados
- Filtros que cruzam informações entre diferentes tabelas
- Diferentes métodos de alteração/exclusão por campo

## 👩‍💻 Autoria

Projeto desenvolvido por alunos do 1º ano do Venus.
