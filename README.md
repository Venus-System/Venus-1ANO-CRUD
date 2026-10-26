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

Telas e arquivos do site:

- `banco/venus.sql` — cria as tabelas, chaves e restrições (PostgreSQL)
- `banco/dataload.sql` — carrega os dados iniciais e ajusta as sequências
- `banco/CONVENCAO_NOMES.md` — convenção de nomes do código e do banco
- `src/main/webapp/index.html` — landing page
- `src/main/webapp/WEB-INF/views/` — telas do painel (JSP), abertas pelos Servlets
- `src/main/webapp/assets/` — CSS, imagens e JavaScript (mesmo visual na landing e no painel)

## ▶️ Como rodar

1. No PostgreSQL, crie um banco vazio (ex.: `venus`) e execute `banco/venus.sql`. Para ter dados de exemplo, execute `banco/dataload.sql` uma única vez, depois da criação das tabelas.
   O arquivo `banco/ajustes_aiven.sql` serve apenas para ajustar bases antigas e não faz parte dessa instalação nova.
2. Copie `.env.example` para `.env` e coloque o endereço, o usuário e a senha do seu banco.
3. Rode o projeto no IntelliJ com o **Tomcat 11** (o projeto usa Servlet 6.1).
4. Abra `http://localhost:8080/venus/` → landing. O botão **Login** abre a tela de login (`/login`); depois de entrar com e-mail e senha da tabela `usuario`, abre o Dashboard.

## ✅ Funcionalidades

- CRUD completo em pelo menos três tabelas do banco de dados
- Filtros que cruzam informações entre diferentes tabelas
- Diferentes métodos de alteração/exclusão por campo

## 👩‍💻 Autoria

Projeto desenvolvido por alunos do 1º ano do Venus.
