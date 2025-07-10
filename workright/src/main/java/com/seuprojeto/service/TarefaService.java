package com.seuprojeto.service;

import com.seuprojeto.model.Historico;
import com.seuprojeto.model.SessaoPomodoro;
import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.repository.HistoricoRepository;
import com.seuprojeto.repository.TarefaRepository;
import com.seuprojeto.security.UsuarioAutenticadoProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;
    private final HistoricoRepository historicoRepository;

    public TarefaService(TarefaRepository tarefaRepository,
                         UsuarioAutenticadoProvider usuarioAutenticadoProvider,
                         HistoricoRepository historicoRepository) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioAutenticadoProvider = usuarioAutenticadoProvider;
        this.historicoRepository = historicoRepository;
    }

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        return tarefaRepository.findByUsuario(usuario);
    }

    public void excluir(Long id) {
        Usuario usuario = usuarioAutenticadoProvider.getUsuarioAutenticado();
        Tarefa tarefa = tarefaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        tarefaRepository.delete(tarefa);
    }

    /**
     * Salva a tarefa e, se for marcada como CONCLUIDA e ainda não estiver no histórico,
     * registra um histórico com base nas sessões concluídas.
     */
    public void salvar(Tarefa novaTarefa) {
        boolean tarefaConcluida = "CONCLUIDA".equalsIgnoreCase(novaTarefa.getStatus());

        Tarefa tarefaOriginal = null;
        if (novaTarefa.getId() != null) {
            tarefaOriginal = tarefaRepository.findById(novaTarefa.getId()).orElse(null);

            // força o carregamento das sessões da nova tarefa
            novaTarefa = tarefaRepository.findById(novaTarefa.getId()).orElseThrow();
            novaTarefa.getSessoes().size(); // força o Hibernate a carregar as sessões

        }

        boolean mudouParaConcluida = tarefaOriginal != null
                && !"CONCLUIDA".equalsIgnoreCase(tarefaOriginal.getStatus())
                && tarefaConcluida;

        tarefaRepository.save(novaTarefa);

        if (mudouParaConcluida && historicoRepository.findByTarefa(novaTarefa).isEmpty()) {
            Historico historico = new Historico();
            historico.setTarefa(novaTarefa);
            historico.setUsuario(novaTarefa.getUsuario());
            historico.setData(LocalDate.now());

            //  Soma TODAS as sessões concluídas da tarefa
            int totalMinutos = novaTarefa.getSessoes().stream()
                    .filter(sessao -> "CONCLUIDA".equalsIgnoreCase(sessao.getStatus()))
                    .mapToInt(sessao -> 
                        (sessao.getDuracao() + sessao.getPausas()) * sessao.getCiclos()
                    )
                    .sum();

            int totalHoras = Math.max(totalMinutos / 60, 1); // garante no mínimo 1 hora
            historico.setTotalHoras(totalHoras);

            historicoRepository.save(historico);
        }
    }

}
