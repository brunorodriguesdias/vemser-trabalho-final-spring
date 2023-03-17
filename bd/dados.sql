-- INSERTS --

-- usuarios --
-- [1] - Companhia

INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'tam123@email.com', 'senhasecreta', 'companhia tam', '0', 1);
INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'gol123@email.com', 'senhagol', 'companhia gol', '0', 1);
INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'azul123@email.com', 'senhaazul', 'companhia azul', '0', 1);
INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'fly123@email.com', 'senhafly', 'companhia fly emirates', '0', 1);
INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'american123@email.com', 'senhaamerican', 'companhia american airlines', '0', 1);

-- [2] - Comprador

INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'castelovski@email.com', 'umasenha', 'kelly castelo', '1', 1);
INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'bruno123@email.com', 'esqueciasenha', 'bruno rodrigues', '1', 1);
INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'alasca123@email.com', 'alascalinda', 'alasca rodrigues', '1', 1);
INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'risadinha@email.com', 'mari123', 'mariana machado', '1', 1);
INSERT INTO AVIACAO.USUARIO (id_usuario, login, senha, nome, tipo, ativo)
	VALUES (AVIACAO.seq_usuario.nextval, 'robs@email.com', 'robervaldo123', 'robervaldo da silva', '1', 1);


-- compradores --

INSERT INTO AVIACAO.COMPRADOR (cpf, id_usuario)
	VALUES ('02805891074', 6);
INSERT INTO AVIACAO.COMPRADOR (cpf, id_usuario)
	VALUES ('62778477080', 7);
INSERT INTO AVIACAO.COMPRADOR (cpf, id_usuario)
	VALUES ('60740558072', 8);
INSERT INTO AVIACAO.COMPRADOR (cpf, id_usuario)
	VALUES ('89990578010', 9);
INSERT INTO AVIACAO.COMPRADOR (cpf, id_usuario)
	VALUES ('56191742045', 10);

-- companhias --

INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('47026248/000195', 'tam aviao', 1);
INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('58407196/000113', 'gol aviao', 2);
INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('74959720/000115', 'azul aviao', 3);
INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('52958019/000149', 'fly emirates aviao', 4);
INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('71160706/000169', 'american airlines aviao', 5);

-- trechos --

INSERT INTO AVIACAO.TRECHO (id_trecho, origem, destino, id_companhia)
	VALUES (AVIACAO.seq_trecho.nextval, 'BEL', 'CWB', 1);
INSERT INTO AVIACAO.TRECHO (id_trecho, origem, destino, id_companhia)
	VALUES (AVIACAO.seq_trecho.nextval, 'POA', 'RJ', 2);
INSERT INTO AVIACAO.TRECHO (id_trecho, origem, destino, id_companhia)
	VALUES (AVIACAO.seq_trecho.nextval, 'SP', 'POA', 3);
INSERT INTO AVIACAO.TRECHO (id_trecho, origem, destino, id_companhia)
	VALUES (AVIACAO.seq_trecho.nextval, 'POA', 'FLO', 4);
INSERT INTO AVIACAO.TRECHO (id_trecho, origem, destino, id_companhia)
	VALUES (AVIACAO.seq_trecho.nextval, 'SP', 'RJ', 5);

-- vendas --

INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 1, TIMESTAMP '2023-03-16 14:30:00', 1, 6, 'd3f84afb-30ba-40bf-a8e4-1fe43e29f81f');

INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 1, TIMESTAMP '2023-03-16 14:30:00', 2, 7, 'b9e19222-412e-43eb-8f37-46ab322e77d3');

INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 1 , TIMESTAMP '2023-03-16 14:30:00', 3, 8, 'd744dfb4-6f57-45fa-96a3-e8c3349b71ec');

INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 1 , TIMESTAMP '2023-03-16 14:30:00', 4, 9, '4e5e9c7d-4b72-4dc4-a984-22bae61473fd');

INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 1 , TIMESTAMP '2023-03-16 14:30:00', 5, 10, 'e0470f31-f930-488c-b6e9-4f15236fabb7');

-- passagens --
-- [0] - false
-- [1] - true

INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, data_partida, data_chegada, disponivel, valor, id_trecho, id_venda)
	VALUES (AVIACAO.seq_passagem.nextval, 'd1a1b20c-a2be-4995-983b-bc386dcee06a',
		TO_TIMESTAMP('17-02-2023 16:18','dd-MM-yyyyHH24:MI'),
        TO_TIMESTAMP('18-02-2023 16:18','dd-MM-yyyyHH24:MI'), 0, 200.9, 1, 1);
	
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, data_partida, data_chegada, disponivel, valor, id_trecho, id_venda)
	VALUES (AVIACAO.seq_passagem.nextval, 'f871a744-7165-4b38-9710-694208b98970',
        TO_TIMESTAMP('17-02-2023 16:18','dd-MM-yyyyHH24:MI'),
	    TO_TIMESTAMP('18-02-2023 16:18','dd-MM-yyyyHH24:MI'), 0, 650, 2, 2);
	
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, data_partida, data_chegada, disponivel, valor, id_trecho, id_venda)
	VALUES (AVIACAO.seq_passagem.nextval, 'ca3b7258-1ef2-450b-98e8-79f5e3ccc3ee',
        TO_TIMESTAMP('17-02-2023 16:18','dd-MM-yyyyHH24:MI'),
	    TO_TIMESTAMP('18-02-2023 16:18','dd-MM-yyyyHH24:MI'), 0, 790.5, 3, 3);
	
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, data_partida, data_chegada, disponivel, valor, id_trecho, id_venda)
	VALUES (AVIACAO.seq_passagem.nextval, '8c8fea7a-6c96-45c4-b4cc-a6873cb0b791',
        TO_TIMESTAMP('17-02-2023 16:18','dd-MM-yyyyHH24:MI'),
	    TO_TIMESTAMP('18-02-2023 16:18','dd-MM-yyyyHH24:MI'), 0, 364.2, 4, 4);
	
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, data_partida, data_chegada, disponivel, valor, id_trecho, id_venda)
	VALUES (AVIACAO.seq_passagem.nextval, 'd440f0fd-6c7d-4b66-9a34-6193abe3ee1d',
	    TO_TIMESTAMP('17-02-2023 16:18','dd-MM-yyyyHH24:MI'),
	    TO_TIMESTAMP('18-02-2023 16:18','dd-MM-yyyyHH24:MI'), 0, 861.8, 5, 5);
	

