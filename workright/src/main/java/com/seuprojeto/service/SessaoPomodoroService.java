package com.seuprojeto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seuprojeto.model.SessaoPomodoro;
import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.repository.SessaoPomodoroRepository;
import com.seuprojeto.service.HistoricoService;

@Service
public class SessaoPomodoroService {

    private final HistoricoService historicoService;

    private final SessaoPomodoroRepository repository;

    public SessaoPomodoroService(SessaoPomodoroRepository repository ,HistoricoService historicoService) {
        this.repository = repository;
        this.historicoService = historicoService;
    }

    public SessaoPomodoro salvar(SessaoPomodoro sessao) {
        return repository.save(sessao);
    }

    public SessaoPomodoro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));
    }

    public void concluirSessao(Long id) {
        SessaoPomodoro sessao = buscarPorId(id);
        sessao.setStatus("CONCLUIDA");
        repository.save(sessao);
        // Registra no histórico
        historicoService.registrarConclusaoSessao(sessao);
    }

    public List<SessaoPomodoro> listarPorTarefa(Tarefa tarefa) {
        return repository.findByTarefaOrderByIdDesc(tarefa);
    }

    public boolean podeCriarNovaSessao(Tarefa tarefa) {
        if ("CONCLUIDA".equalsIgnoreCase(tarefa.getStatus())) {
            return false;
        }
        List<SessaoPomodoro> sessoes = listarPorTarefa(tarefa);
        return sessoes.isEmpty() || "CONCLUIDA".equalsIgnoreCase(sessoes.get(0).getStatus());
    }
    public List<SessaoPomodoro> listarPorUsuario(Usuario usuario) {
        return repository.findByTarefaUsuarioOrderByHoraInicioDesc(usuario);
    }
    public void excluirPorId(Long id) {
        repository.deleteById(id);
    }
    

}