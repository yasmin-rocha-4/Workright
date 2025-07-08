package com.seuprojeto.repository;

import com.seuprojeto.model.Usuario;
import com.seuprojeto.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByUsuario(Usuario usuario);

}