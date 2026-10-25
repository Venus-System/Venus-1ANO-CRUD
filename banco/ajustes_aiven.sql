SELECT setval(pg_get_serial_sequence('nome_ingrediente', 'id_nome'), (SELECT MAX(id_nome) FROM nome_ingrediente));
SELECT setval(pg_get_serial_sequence('produto_ingrediente', 'id_produto_ingrediente'), (SELECT MAX(id_produto_ingrediente) FROM produto_ingrediente));
SELECT setval(pg_get_serial_sequence('produto_usuario', 'id_produto_usuario'), (SELECT MAX(id_produto_usuario) FROM produto_usuario));

ALTER TABLE produto DROP CONSTRAINT produto_pontuacao_check;
ALTER TABLE produto ADD CONSTRAINT produto_pontuacao_check CHECK (pontuacao BETWEEN 0 AND 100);

UPDATE produto SET pontuacao = pontuacao * 10 WHERE pontuacao <= 10;
UPDATE analise SET pontuacao = pontuacao * 10 WHERE pontuacao <= 10;
