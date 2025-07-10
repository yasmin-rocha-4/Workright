package com.seuprojeto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByUsuario(Usuario usuario);
    Optional<Tarefa> findByIdAndUsuario(Long id, Usuario usuario); 
}