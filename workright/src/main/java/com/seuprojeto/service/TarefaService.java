package com.seuprojeto.service;

import com.seuprojeto.model.Tarefa;
import com.seuprojeto.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    public void salvar(Tarefa tarefa) {
        tarefaRepository.save(tarefa);
    }

    public void excluir(Long id) {
        tarefaRepository.deleteById(id);
    }
}
