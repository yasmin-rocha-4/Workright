
package com.seuprojeto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.repository.TarefaRepository;
import com.seuprojeto.security.UsuarioAutenticadoProvider;
import com.seuprojeto.service.TarefaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final TarefaRepository tarefaRepository; 
    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;


    public TarefaController(TarefaService tarefaService,
                          TarefaRepository tarefaRepository, 
                          UsuarioAutenticadoProvider usuarioAutenticadoProvider) {
        this.tarefaService = tarefaService;
        this.tarefaRepository = tarefaRepository; 
        this.usuarioAutenticadoProvider = usuarioAutenticadoProvider;
    }


    @GetMapping
    public String listar(Model model) {
        Usuario usuario = usuarioAutenticadoProvider.getUsuarioAutenticado();
        List<Tarefa> tarefasDoUsuario = tarefaService.listarPorUsuario(usuario);
        model.addAttribute("tarefas", tarefasDoUsuario);
        return "tarefas/listar";
}


    @GetMapping("/nova")
    public String novaTarefa(Model model) {
        model.addAttribute("tarefa", new Tarefa());
        model.addAttribute("usuario", usuarioAutenticadoProvider.getUsuarioAutenticado());
        return "tarefas/formulario";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Tarefa tarefa, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuarioAutenticadoProvider.getUsuarioAutenticado());
            return "tarefas/formulario";
        }
        
        try {
            // Garante que o usuário está associado à tarefa
            Usuario usuario = usuarioAutenticadoProvider.getUsuarioAutenticado();
            tarefa.setUsuario(usuario);
            
            // Salva no banco
            Tarefa tarefaSalva = tarefaService.salvarTarefa(tarefa);
            return "redirect:/tarefas/editar/" + tarefaSalva.getId();
            
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar tarefa: " + e.getMessage());
            return "tarefas/formulario";
        }
    }


   @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioAutenticadoProvider.getUsuarioAutenticado();
        Tarefa tarefa = tarefaRepository.findByIdAndUsuario(id, usuario)
            .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou não pertence ao usuário"));
        
        model.addAttribute("tarefa", tarefa);
        model.addAttribute("usuario", usuario);
        return "tarefas/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
        return "redirect:/tarefas";
    }
}