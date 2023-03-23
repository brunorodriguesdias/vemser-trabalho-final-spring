-- INSERTS --

-- usuarios --
-- [1] - Companhia
--DELETE VENDA;
--DELETE PASSAGEM;
--DELETE TRECHO;
--DELETE COMPANHIA;
--DELETE COMPRADOR;
--DELETE USUARIO;
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
	VALUES ('47.026.248/0001-95', 'tam aviao', 1);
INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('58.407.196/0001-13', 'gol aviao', 2);
INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('74.959.720/0001-15', 'azul aviao', 3);
INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('52.958.019/0001-49', 'fly emirates aviao', 4);
INSERT INTO AVIACAO.COMPANHIA (cnpj, nome_fantasia, id_usuario)
	VALUES ('71.160.706/0001-69', 'american airlines aviao', 5);

-- aviao --

INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PT-ZXE', 330, TO_TIMESTAMP('17-02-2023','dd-MM-yyyy'), 1, 1);
INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PP-GHJ', 620, TO_TIMESTAMP('20-03-2023','dd-MM-yyyy'), 2, 1);
INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao,capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PP-ASD', 490, TO_TIMESTAMP('01-03-2023','dd-MM-yyyy'), 3, 1);
INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PT-UTY', 670, TO_TIMESTAMP('10-03-2023','dd-MM-yyyy'), 4, 1);
INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PT-POI', 800, TO_TIMESTAMP('05-02-2023','dd-MM-yyyy'), 5, 1);

INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PP-YJK', 872, TO_TIMESTAMP('01-02-2023','dd-MM-yyyy'), 1, 1);
INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PP-LKJ', 543, TO_TIMESTAMP('08-02-2023','dd-MM-yyyy'), 2, 1);
INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao,capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PT-OLÇ', 906, TO_TIMESTAMP('21-03-2023','dd-MM-yyyy'), 3, 1);
INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PR-WEE', 175, TO_TIMESTAMP('23-03-2023','dd-MM-yyyy'), 4, 1);
INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
	VALUES (AVIACAO.seq_aviao.nextval, 'PR-TTT', 689, TO_TIMESTAMP('19-03-2023','dd-MM-yyyy'), 5, 1);

--INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
--	VALUES (AVIACAO.seq_aviao.nextval, 'PU-YTY', 247, TO_TIMESTAMP('24-02-2023','dd-MM-yyyy'), 1, 1);
--INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
--	VALUES (AVIACAO.seq_aviao.nextval, 'PU-MBH', 318, TO_TIMESTAMP('13-02-2023','dd-MM-yyyy'), 2, 1);
--INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao,capacidade, ultima_manutencao, id_companhia, ativo)
--	VALUES (AVIACAO.seq_aviao.nextval, 'PU-CGK', 764, TO_TIMESTAMP('10-02-2023','dd-MM-yyyy'), 3, 1);
--INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
--	VALUES (AVIACAO.seq_aviao.nextval, 'PP-VKL', 529, TO_TIMESTAMP('21-02-2023','dd-MM-yyyy'), 4, 1);
--INSERT INTO AVIACAO.AVIAO(id_aviao, codigo_aviao, capacidade, ultima_manutencao, id_companhia, ativo)
--	VALUES (AVIACAO.seq_aviao.nextval, 'PT-PÇL', 431, TO_TIMESTAMP('01-03-2023','dd-MM-yyyy'), 5, 1);


-- voo --

INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Porto Alegre', 'Salvador',
		TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 1, 2);
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'São Paulo', 'Rio de Janeiro',
        TO_TIMESTAMP('18-03-2023 08:30','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('18-03-2023 10:00','dd-MM-yyyyHH24:MI'), 150, 2, 2);
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Curitiba', 'Florianópolis',
        TO_TIMESTAMP('19-03-2023 13:15','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('19-03-2023 14:30','dd-MM-yyyyHH24:MI'), 100, 3, 2);
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Belo Horizonte', 'Brasília',
        TO_TIMESTAMP('20-03-2023 16:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('20-03-2023 17:30','dd-MM-yyyyHH24:MI'), 175, 4, 2);
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Rio de Janeiro', 'Curitiba',
		TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 5, 2);
  
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 6, 2);
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 7, 2);
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 8, 2);
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 9, 2);
INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 10, 2);

--INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
--	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
--	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 11, 2);
--INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
--	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
--	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 12, 2);
--INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
--	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
--	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 13, 2);
--INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
--	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
--	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 14, 2);
--INSERT INTO AVIACAO.VOO(id_voo, origem, destino, data_partida, data_chegada, assentos_disponiveis, id_aviao, status)
--	VALUES (AVIACAO.seq_voo.nextval, 'Manaus', 'Florianópolis',
--	TO_TIMESTAMP('17-03-2023 12:00','dd-MM-yyyyHH24:MI'), TO_TIMESTAMP('17-03-2023 20:00','dd-MM-yyyyHH24:MI'), 200, 15, 2);


--/ PASSAGENS /--
-- status --
-- [0] - nunca pode ser 0
-- [1] - cancelado
-- [2] - disponivel
-- [3] - vendida

-- tipo assento --
-- [1] - clássico
-- [2] - executivo
-- [3] - econômico

INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'd111110c-a2be-4995-983b-bc386dcee06a', 2, 200.0, 'A1', 1, 1, 1 );
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'f333330e-a4cg-4997-983d-bc386dcee06c', 2, 300.0, 'A1', 1, 2, 2 );
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'h555550g-a6ei-4999-983f-bc386dcee06e', 2, 800.0, 'A1', 2, 3, 3 );
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'w888881s-b3km-2345-678r-hj098uyt67i', 2, 700.0, 'A1', 2, 4, 4 );
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'd666662p-e9th-4567-890y-po098ujh65t', 2, 600.0, 'A1', 2, 5, 5 );


INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'e222220d-a3bf-4996-983c-bc386dcee06b', 2, 800.0, 'A1', 2, 1, 6 );
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'g444440f-a5dh-4998-983e-bc386dcee06d', 2, 120.0, 'A1', 3, 2, 7 );
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 't222220r-z8uj-9876-765t-df743ehg87r', 2, 400.0, 'A1', 2, 3, 8 );
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'f444441a-x2bn-3456-789t-lk765jhg43s', 2, 100.0, 'A1', 3, 4, 9 );
INSERT INTO AVIACAO.PASSAGEM (id_passagem, codigo, status, valor, numero_assento, tipo_assento, id_companhia, id_voo)
	VALUES (AVIACAO.seq_passagem.nextval, 'j111116c-q7fg-5678-901u-mn432lkj10q', 2, 150.0, 'A1', 3, 5, 10 );


--/ VENDAS --/

INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, id_passagem, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 0, SYSDATE, 1, 6, 1, 'd3f84afb-30ba-40bf-a8e4-1fe43e29f81f');
INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, id_passagem, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 0, SYSDATE, 2, 7, 2, 'b9e19222-412e-43eb-8f37-46ab322e77d3');
INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, id_passagem, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 0 , SYSDATE, 3, 8, 3, 'd744dfb4-6f57-45fa-96a3-e8c3349b71ec');
INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, id_passagem, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 0 , SYSDATE, 4, 9, 4, '4e5e9c7d-4b72-4dc4-a984-22bae61473fd');
INSERT INTO AVIACAO.VENDA  (id_venda, status, data, id_companhia, id_comprador, id_passagem, codigo)
	VALUES (AVIACAO.seq_venda.nextval, 0 , SYSDATE, 5, 10, 5, 'e0470f31-f930-488c-b6e9-4f15236fabb7');

UPDATE AVIACAO.PASSAGEM SET ID_VENDA = 1, STATUS = 3 WHERE ID_PASSAGEM = 1;
UPDATE AVIACAO.PASSAGEM SET ID_VENDA = 2, STATUS = 3 WHERE ID_PASSAGEM = 2;
UPDATE AVIACAO.PASSAGEM SET ID_VENDA = 3, STATUS = 3 WHERE ID_PASSAGEM = 3;
UPDATE AVIACAO.PASSAGEM SET ID_VENDA = 4, STATUS = 3 WHERE ID_PASSAGEM = 4;
UPDATE AVIACAO.PASSAGEM SET ID_VENDA = 5, STATUS = 3 WHERE ID_PASSAGEM = 5;