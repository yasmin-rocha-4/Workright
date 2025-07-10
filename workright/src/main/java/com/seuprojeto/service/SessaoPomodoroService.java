package com.seuprojeto.service;

import com.seuprojeto.model.SessaoPomodoro;
import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.repository.SessaoPomodoroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessaoPomodoroService {

    private final SessaoPomodoroRepository repository;

    public SessaoPomodoroService(SessaoPomodoroRepository repository) {
        this.repository = repository;
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