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
     * Salva a tarefa e, se for marcada como CONCLUIDA e ainda não estiver no
     * histórico,
     * registra um histórico com base nas sessões concluídas.
     */
    public void salvar(Tarefa novaTarefa) {
        boolean marcandoComoConcluida = "CONCLUIDA".equalsIgnoreCase(novaTarefa.getStatus());

        Tarefa tarefaAntiga = null;

        if (novaTarefa.getId() != null) {
            tarefaAntiga = tarefaRepository.findById(novaTarefa.getId()).orElse(null);

            Tarefa tarefaSalva = tarefaRepository.findById(novaTarefa.getId()).orElseThrow();
            tarefaSalva.setTitulo(novaTarefa.getTitulo());
            tarefaSalva.setDescricao(novaTarefa.getDescricao());
            tarefaSalva.setStatus(novaTarefa.getStatus());
            tarefaSalva.setPrazo(novaTarefa.getPrazo());

            tarefaSalva.getSessoes().size(); // carrega sessões para o cálculo posterior
            novaTarefa = tarefaSalva;

        }

        boolean mudouParaConcluida = tarefaAntiga != null
                && !"CONCLUIDA".equalsIgnoreCase(tarefaAntiga.getStatus())
                && marcandoComoConcluida;

        tarefaRepository.save(novaTarefa);

        // Se mudou para concluída e ainda não há histórico, criar novo registro
        if (mudouParaConcluida && historicoRepository.findByTarefa(novaTarefa).isEmpty()) {
            Historico historico = new Historico();
            historico.setTarefa(novaTarefa);
            historico.setUsuario(novaTarefa.getUsuario());
            historico.setData(LocalDate.now());

            // Soma tempo das sessões CONCLUÍDAS
            int minutosTotais = novaTarefa.getSessoes().stream()
                    .filter(sessao -> "CONCLUIDA".equalsIgnoreCase(sessao.getStatus()))
                    .mapToInt(sessao -> (sessao.getDuracao() + sessao.getPausas()) * sessao.getCiclos())
                    .sum();

            int horasTotais = Math.max(minutosTotais / 60, 1);
            historico.setTotalHoras(horasTotais);

            historicoRepository.save(historico);
        }
    }
}
