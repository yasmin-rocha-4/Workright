package com.seuprojeto.repository;

import com.seuprojeto.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
}