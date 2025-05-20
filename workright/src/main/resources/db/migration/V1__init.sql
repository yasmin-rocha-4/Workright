CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    senha VARCHAR(255),
    papel VARCHAR(50)
);

CREATE TABLE tarefa (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255),
    descricao TEXT,
    status VARCHAR(50),
    prazo DATE,
    usuario_id BIGINT REFERENCES usuario(id)
);

CREATE TABLE sessao_pomodoro (
    id SERIAL PRIMARY KEY,
    duracao INT,
    pausas INT,
    ciclos INT,
    hora_inicio TIME,
    status VARCHAR(50),
    tarefa_id BIGINT REFERENCES tarefa(id)
);

CREATE TABLE historico (
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuario(id),
    tarefa_id BIGINT REFERENCES tarefa(id),
    data DATE,
    total_horas INT
);