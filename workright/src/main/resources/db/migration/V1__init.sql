-- Criação da tabela usuario
CREATE TABLE usuario (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    papel VARCHAR(50)
);

-- Inserção do usuário admin
INSERT INTO usuario (nome, email, senha, papel)
VALUES ('Admin', 'admin@admin.com', '123456789012', 'ADMIN');

-- Criação da tabela tarefa
CREATE TABLE tarefa (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo VARCHAR(255),
    descricao TEXT,
    status VARCHAR(50),
    prazo DATE,
    usuario_id BIGINT REFERENCES usuario(id)
);

-- Criação da tabela sessao_pomodoro
CREATE TABLE sessao_pomodoro (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    duracao INT,
    pausas INT,
    ciclos INT,
    hora_inicio TIME,
    status VARCHAR(50),
    tarefa_id BIGINT REFERENCES tarefa(id)
);

-- Criação da tabela historico
CREATE TABLE historico (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuario(id),
    tarefa_id BIGINT REFERENCES tarefa(id),
    data DATE,
    total_horas INT
);
