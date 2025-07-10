package com.seuprojeto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seuprojeto.model.SessaoPomodoro;
import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;

public interface SessaoPomodoroRepository extends JpaRepository<SessaoPomodoro, Long> {
    List<SessaoPomodoro> findByTarefaOrderByIdDesc(Tarefa tarefa);
    List<SessaoPomodoro> findByTarefaUsuarioOrderByHoraInicioDesc(Usuario usuario);
}