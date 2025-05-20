package com.seuprojeto.repository;

import com.seuprojeto.model.SessaoPomodoro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoPomodoroRepository extends JpaRepository<SessaoPomodoro, Long> {
}