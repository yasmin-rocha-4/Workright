
-- Verifica e cria a coluna tarefa_id caso ainda não exista
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'sessao_pomodoro' AND column_name = 'tarefa_id'
    ) THEN
        ALTER TABLE sessao_pomodoro
        ADD COLUMN tarefa_id BIGINT;
    END IF;
END$$;

-- Adiciona a constraint de chave estrangeira (se ainda não existir)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_sessao_tarefa'
    ) THEN
        ALTER TABLE sessao_pomodoro
        ADD CONSTRAINT fk_sessao_tarefa
        FOREIGN KEY (tarefa_id)
        REFERENCES tarefa(id)
        ON DELETE CASCADE;
    END IF;
END$$;
