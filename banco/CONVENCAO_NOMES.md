# Convenção de nomenclatura

## Banco de dados

- Tabelas e colunas usam letras minúsculas e `snake_case`, sem acentos: `usuario_alergia`, `dt_nascimento`.
- Chaves primárias usam `id_<entidade>`; tabelas de relação mantêm as chaves estrangeiras `id_<entidade_relacionada>`.
- Restrições usam os prefixos `pk_`, `fk_`, `uq_` e `ck_` quando recebem um nome explícito.
- Datas começam com `dt_`; campos booleanos começam com `eh_`, `prefere_` ou outro verbo que expresse a condição.

## Java e interface

- Pacotes e atributos usam letras minúsculas; classes usam `PascalCase` e métodos/atributos usam `camelCase`.
- Classes de acesso ao banco terminam em `DAO`, e endpoints HTTP terminam em `Servlet`.
- Parâmetros de formulário seguem o nome da coluna SQL quando representam diretamente um campo do banco. A conversão para `camelCase` acontece no modelo Java.
- Arquivos JSP usam nomes descritivos em `snake_case`, com os prefixos `cadastro_`, `lista_` e `editar_` para as telas de CRUD.

Os nomes existentes de tabelas, URLs e parâmetros permanecem estáveis para preservar os dados e os links já usados pelo projeto.
