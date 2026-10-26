-- ---------- TABELAS ----------

CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome_completo VARCHAR(100),
    genero VARCHAR(25),
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(60) NOT NULL,
    dt_nascimento DATE NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    dt_cadastro DATE DEFAULT CURRENT_DATE,
    CHECK (EXTRACT(YEAR FROM age(CURRENT_DATE, dt_nascimento)) >= 18),
    CHECK (email LIKE '%@%.%'),
    CHECK (LENGTH(senha) >= 6),
    CHECK (genero IN ('Feminino', 'Masculino', 'Outro', 'Prefiro não informar')),
    CHECK (LENGTH(telefone) BETWEEN 10 AND 13),
    CHECK (dt_nascimento > '1900-01-01'),
    CHECK (dt_cadastro <= CURRENT_DATE));

CREATE TABLE produto (
    id_produto SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    marca VARCHAR(50),
    categoria VARCHAR(50),
    descricao VARCHAR(255),
    eh_vegano BOOLEAN,
    eh_cruelty_free BOOLEAN,
    pontuacao INT,
    lista_ingredientes VARCHAR(255),
    CHECK (pontuacao BETWEEN 0 AND 100));

CREATE TABLE ingredientes (
    id_ingrediente SERIAL PRIMARY KEY,
    nivel_perigo INT,
    tipo VARCHAR(100),
    dt_atualizacao DATE DEFAULT CURRENT_DATE,
    CHECK (nivel_perigo BETWEEN 1 AND 5));

CREATE TABLE preferencias (
    id_preferencias SERIAL PRIMARY KEY,
    faixa_preco VARCHAR(20),
    prefere_vegano BOOLEAN,
    restricoes_dieta VARCHAR(100),
    categorias_pref VARCHAR(100),
    marcas_fav VARCHAR(100),
    id_usuario INT REFERENCES usuario(id_usuario));

CREATE TABLE alergia (
    id_alergia SERIAL PRIMARY KEY,
    nome_alergia VARCHAR(100));

CREATE TABLE analise (
    id_analise SERIAL PRIMARY KEY,
    dt_hr_analise TIMESTAMP,
    resumo_resultado VARCHAR(255),
    pontuacao INT,
    id_usuario INT REFERENCES usuario(id_usuario),
    CHECK (pontuacao BETWEEN 0 AND 100));

CREATE TABLE ingrediente_analise (
    id_ingrediente_analise SERIAL PRIMARY KEY,
    id_ingrediente INT REFERENCES ingredientes(id_ingrediente),
    id_analise INT REFERENCES analise(id_analise));

CREATE TABLE perfil_pele (
    id_perfil_pele SERIAL PRIMARY KEY,
    sensibilidade INT,
    tipo_pele VARCHAR(20),
    nivel_oleosidade INT,
    id_usuario INT REFERENCES usuario(id_usuario),
    CHECK (sensibilidade BETWEEN 1 AND 10),
    CHECK (nivel_oleosidade BETWEEN 1 AND 10),
    CHECK (tipo_pele IN ('Seca', 'Oleosa', 'Sensível', 'Mista', 'Normal')));

CREATE TABLE usuario_alergia (
    id_usuario_alergia SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES usuario(id_usuario),
    id_alergia INT REFERENCES alergia(id_alergia),
    grau INT,
    dt_registro DATE DEFAULT CURRENT_DATE,
    CHECK (grau BETWEEN 1 AND 5),
    CHECK (dt_registro <= CURRENT_DATE));

ALTER TABLE usuario_alergia
ADD CONSTRAINT uq_usuario_alergia UNIQUE (id_usuario, id_alergia);

CREATE TABLE produto_usuario (
    id_produto_usuario SERIAL PRIMARY KEY,
    id_produto INT REFERENCES produto(id_produto),
    id_usuario INT REFERENCES usuario(id_usuario));

CREATE TABLE perfil_cabelo (
    id_perfil_cabelo SERIAL PRIMARY KEY,
    curvatura INT,
    oleosidade INT,
    espessura INT,
    id_usuario INT REFERENCES usuario(id_usuario),
    CHECK (curvatura BETWEEN 1 AND 4),
    CHECK (oleosidade BETWEEN 1 AND 10),
    CHECK (espessura BETWEEN 1 AND 10));

CREATE TABLE nome_ingrediente (
    id_nome SERIAL PRIMARY KEY,
    nome_ingrediente VARCHAR(100),
    id_ingrediente INT REFERENCES ingredientes(id_ingrediente));

CREATE TABLE produto_ingrediente (
    id_produto_ingrediente SERIAL PRIMARY KEY,
    id_ingrediente INT REFERENCES ingredientes(id_ingrediente),
    id_produto INT REFERENCES produto(id_produto));

CREATE TABLE identifica (
    id_ingrediente INT,
    id_analise INT,
    PRIMARY KEY (id_ingrediente, id_analise));

CREATE TABLE recomenda (
    id_produto INT,
    id_usuario INT,
    PRIMARY KEY (id_produto, id_usuario));

-- A aplicação normaliza e-mails em minúsculas. O índice também protege
-- cadastros simultâneos e dados inseridos fora da aplicação.
CREATE UNIQUE INDEX uq_usuario_email ON usuario (LOWER(email));

ALTER TABLE produto_usuario
ADD CONSTRAINT uq_produto_usuario UNIQUE (id_produto, id_usuario);

ALTER TABLE identifica
ADD CONSTRAINT fk_identifica_ingrediente FOREIGN KEY (id_ingrediente) REFERENCES ingredientes(id_ingrediente),
ADD CONSTRAINT fk_identifica_analise FOREIGN KEY (id_analise) REFERENCES analise(id_analise);

ALTER TABLE recomenda
ADD CONSTRAINT fk_recomenda_produto FOREIGN KEY (id_produto) REFERENCES produto(id_produto),
ADD CONSTRAINT fk_recomenda_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario);

ALTER TABLE alergia ADD UNIQUE (nome_alergia);

ALTER TABLE produto_ingrediente
ADD CONSTRAINT uq_produto_ingrediente UNIQUE (id_produto, id_ingrediente);

ALTER TABLE ingrediente_analise
ADD CONSTRAINT uq_ingrediente_analise UNIQUE (id_ingrediente, id_analise);
