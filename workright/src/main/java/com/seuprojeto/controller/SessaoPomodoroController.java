package com.seuprojeto.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.seuprojeto.model.Historico;
import com.seuprojeto.model.SessaoPomodoro;
import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.security.UsuarioAutenticadoProvider;
import com.seuprojeto.service.HistoricoService;
import com.seuprojeto.service.SessaoPomodoroService;
import com.seuprojeto.service.TarefaService;

@Controller
@RequestMapping("/sessoes")
public class SessaoPomodoroController {

    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;
    private final HistoricoService historicoService;
    private final SessaoPomodoroService sessaoService;
    private final TarefaService tarefaService;

    public SessaoPomodoroController(
            SessaoPomodoroService sessaoService,
            TarefaService tarefaService,
            UsuarioAutenticadoProvider usuarioAutenticadoProvider,
            HistoricoService historicoService) {
        this.sessaoService = sessaoService;
        this.tarefaService = tarefaService;
        this.usuarioAutenticadoProvider = usuarioAutenticadoProvider;
        this.historicoService = historicoService;
    }

    @GetMapping("/nova/{tarefaId}")
    public String showForm(@PathVariable Long tarefaId, Model model) {
        Tarefa tarefa = tarefaService.buscarPorId(tarefaId);
        SessaoPomodoro sessao = new SessaoPomodoro();
        sessao.setTarefa(tarefa);
        model.addAttribute("sessao", sessao);
        return "pomodoro/formulario";
    }

    @GetMapping("/pomodoro/view/{id}")
    public String showPomodoroView(@PathVariable Long id, Model model) {
        SessaoPomodoro sessao = sessaoService.buscarPorId(id);
        model.addAttribute("sessao", sessao);
        return "pomodoro/view";
    }

    @GetMapping("")
    public String listarSessoes(Model model) {
        Usuario usuario = usuarioAutenticadoProvider.getUsuarioAutenticado();
        List<SessaoPomodoro> sessoes = sessaoService.listarPorUsuario(usuario);
        model.addAttribute("sessoes", sessoes);
        return "pomodoro/listar";
    }

    @PostMapping("/salvar")
    public String saveSession(@ModelAttribute SessaoPomodoro sessao, RedirectAttributes redirectAttributes) {
        sessao.setHoraInicio(LocalTime.now());
        sessao.setStatus("EM_ANDAMENTO");
        sessaoService.salvar(sessao);
        redirectAttributes.addFlashAttribute("success", "Sessão criada com sucesso!");
        return "redirect:/tarefas/editar/" + sessao.getTarefa().getId();
    }

    @GetMapping("/iniciar/{id}")
    public String startSession(@PathVariable Long id) {
        SessaoPomodoro sessao = sessaoService.buscarPorId(id);
        sessao.setStatus("EM_ANDAMENTO");
        sessaoService.salvar(sessao);
        return "redirect:/pomodoro/visualizar/" + id;
    }

    @PostMapping("/concluir/{id}")
    public String concluirSessao(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            SessaoPomodoro sessao = sessaoService.buscarPorId(id);
            sessao.setStatus("CONCLUIDA");
            sessaoService.salvar(sessao);

            // Verifica se a tarefa já foi concluída
            if ("CONCLUIDA".equalsIgnoreCase(sessao.getTarefa().getStatus())) {
                historicoService.registrarConclusaoSessao(sessao);
            }

            redirectAttributes.addFlashAttribute("success", "Sessão concluída com sucesso!");
            return "redirect:/tarefas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao concluir sessão: " + e.getMessage());
            return "redirect:/sessoes/pomodoro/view/" + id;
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirSessao(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        sessaoService.excluirPorId(id);
        redirectAttributes.addFlashAttribute("success", "Sessão excluída com sucesso!");
        return "redirect:/sessoes";
    }
}
