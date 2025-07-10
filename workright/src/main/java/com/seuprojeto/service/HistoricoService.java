package com.seuprojeto.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seuprojeto.model.Historico;
import com.seuprojeto.model.SessaoPomodoro;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.repository.HistoricoRepository;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    public HistoricoService(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    public List<Historico> findByUsuario(Usuario usuario) {
        return historicoRepository.findByUsuarioOrderByDataDesc(usuario);
    }

    public Map<String, Object> getProgressoDiario(Usuario usuario, LocalDate data) {
        List<Historico> registros = historicoRepository.findByUsuarioAndData(usuario, data);
        int totalHoras = registros.stream()
            .mapToInt(Historico::getTotalHoras)
            .sum();

        return Map.of(
            "data", data,
            "totalHoras", totalHoras,
            "metaAtingida", totalHoras >= 8
        );
    }

    @Transactional
    public void registrarConclusaoSessao(SessaoPomodoro sessao) {
        Historico historico = new Historico();
        historico.setUsuario(sessao.getTarefa().getUsuario());
        historico.setTarefa(sessao.getTarefa());
        historico.setData(LocalDate.now());

        int tempoAtivo = sessao.getDuracao();   // minutos
        int tempoPausa = sessao.getPausas();   // minutos
        int ciclos = sessao.getCiclos();           // número de ciclos

        int duracaoTotalMin = (tempoAtivo + tempoPausa) * ciclos;
        int totalHoras = Math.max(duracaoTotalMin / 60, 1);  // arredonda para no mínimo 1h

        historico.setTotalHoras(totalHoras);
        historicoRepository.save(historico);
    }

    public List<Historico> getHistoricoMensal(Usuario usuario) {
        LocalDate hoje = LocalDate.now();
        int mes = hoje.getMonthValue();
        int ano = hoje.getYear();
        return historicoRepository.findByUsuarioAndMes(usuario, mes, ano);
    }

    public Map<String, Object> getProgressoMensal(Usuario usuario) {
        List<Historico> registros = getHistoricoMensal(usuario);

        long tarefasConcluidas = registros.stream()
            .filter(h -> h.getTarefa().getStatus().equalsIgnoreCase("CONCLUIDA"))
            .map(h -> h.getTarefa().getId())
            .distinct()
            .count();

        int totalHoras = registros.stream()
            .mapToInt(Historico::getTotalHoras)
            .sum();

        return Map.of(
            "tarefasConcluidas", tarefasConcluidas,
            "totalHoras", totalHoras
        );
    }

}
