
package com.seuprojeto.controller;

import com.seuprojeto.model.Tarefa;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.security.UsuarioAutenticadoProvider;
import com.seuprojeto.repository.TarefaRepository;
import com.seuprojeto.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;

    public TarefaController(TarefaService tarefaService,
                            UsuarioAutenticadoProvider usuarioAutenticadoProvider) {
        this.tarefaService = tarefaService;
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
    public String salvar(@Valid Tarefa tarefa, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuarioAutenticadoProvider.getUsuarioAutenticado());
            return "tarefas/formulario";
        }
        tarefaService.criarTarefa(tarefa);
        return "redirect:/tarefas";
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("tarefa", tarefaService.buscarPorId(id));
        model.addAttribute("usuario", usuarioAutenticadoProvider.getUsuarioAutenticado());
        return "tarefas/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
        return "redirect:/tarefas";
    }
}