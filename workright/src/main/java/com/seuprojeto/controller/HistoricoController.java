package com.seuprojeto.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seuprojeto.model.Historico;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.service.HistoricoService;
import com.seuprojeto.service.UsuarioService;
@Controller
@RequestMapping("/historico")
public class HistoricoController {

    private final HistoricoService historicoService;
    private final UsuarioService usuarioService;

    public HistoricoController(HistoricoService historicoService, 
                             UsuarioService usuarioService) {
        this.historicoService = historicoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listar")
    public String listarHistorico(Principal principal, Model model) {
        try {
            Usuario usuario = usuarioService.findByEmail(principal.getName());
            List<Historico> historicos = historicoService.findByUsuario(usuario);
            model.addAttribute("historicos", historicos);
            return "historico/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar histórico");
            return "error";
        }
    }

    @GetMapping("/progresso")
    @ResponseBody
    public String progressoDiario(Principal principal) {
        Usuario usuario = usuarioService.findByEmail(principal.getName());
        LocalDate hoje = LocalDate.now();
        
        Map<String, Object> progresso = historicoService.getProgressoDiario(usuario, hoje);
        
        // Retorna um fragmento HTML renderizado
        return """
            <div class="bg-white p-4 rounded-lg shadow mb-4">
                <h3 class="text-lg font-semibold mb-2">Progresso Diário</h3>
                <p class="text-gray-600">Data: %s</p>
                <p class="text-gray-600">Total de horas: %d</p>
                <p class="%s">%s</p>
                <div class="w-full bg-gray-200 rounded-full h-2.5 mt-2">
                    <div class="bg-blue-600 h-2.5 rounded-full" style="width: %d%%"></div>
                </div>
            </div>
            """.formatted(
                progresso.get("data").toString(),
                progresso.get("totalHoras"),
                (boolean)progresso.get("metaAtingida") ? "text-green-500" : "text-yellow-500",
                (boolean)progresso.get("metaAtingida") ? "Meta atingida! 🎉" : "Continue trabalhando!",
                Math.min((int)progresso.get("totalHoras") * 100 / 8, 100)
            );
    }
    @GetMapping("/progressoMensal")
    @ResponseBody
    public String progressoMensal(Principal principal) {
        Usuario usuario = usuarioService.findByEmail(principal.getName());
        List<Historico> historicos = historicoService.getHistoricoMensal(usuario);

        long tarefasUnicas = historicos.stream()
            .map(h -> h.getTarefa().getId())
            .distinct()
            .count();

        int totalHoras = historicos.stream().mapToInt(Historico::getTotalHoras).sum();

        return """
            <div class="bg-white p-4 rounded-lg shadow mb-4">
                <h3 class="text-lg font-semibold mb-2">Progresso Mensal</h3>
                <p class="text-gray-600">Tarefas concluídas no mês: %d</p>
                <p class="text-gray-600">Total de horas registradas: %d</p>
            </div>
        """.formatted(tarefasUnicas, totalHoras);
    }

}