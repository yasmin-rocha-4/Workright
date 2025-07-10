package com.seuprojeto.repository;

import com.seuprojeto.model.Historico;
import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByUsuarioOrderByDataDesc(Usuario usuario);
    List<Historico> findByUsuarioAndDataBetween(Usuario usuario, LocalDate inicio, LocalDate fim);
    List<Historico> findByTarefa(Tarefa tarefa);
    List<Historico> findByUsuarioAndData(Usuario usuario, LocalDate data);
    @Query("SELECT h FROM Historico h WHERE h.usuario = :usuario AND MONTH(h.data) = :mes AND YEAR(h.data) = :ano ORDER BY h.data DESC")
    List<Historico> findByUsuarioAndMes(@Param("usuario") Usuario usuario,
                                    @Param("mes") int mes,
                                    @Param("ano") int ano);

}