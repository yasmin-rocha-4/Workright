package com.seuprojeto.service;

import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.repository.TarefaRepository;
import com.seuprojeto.security.UsuarioAutenticadoProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;

    public TarefaService(TarefaRepository tarefaRepository,
                       UsuarioAutenticadoProvider usuarioAutenticadoProvider) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioAutenticadoProvider = usuarioAutenticadoProvider;
    }

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        return tarefaRepository.findByUsuario(usuario);
    }

    public Tarefa salvarTarefa(Tarefa tarefa) {
        Usuario usuario = usuarioAutenticadoProvider.getUsuarioAutenticado();
        tarefa.setUsuario(usuario);
        return tarefaRepository.save(tarefa);
    }

    public void excluir(Long id) {
        Usuario usuario = usuarioAutenticadoProvider.getUsuarioAutenticado();
        Tarefa tarefa = tarefaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        tarefaRepository.delete(tarefa);
    }
}