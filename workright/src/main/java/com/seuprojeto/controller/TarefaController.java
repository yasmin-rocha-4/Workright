
package com.seuprojeto.controller;

import com.seuprojeto.model.Tarefa;
import com.seuprojeto.repository.TarefaRepository;
import com.seuprojeto.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tarefas", tarefaService.listarTodas());
        return "tarefas/listar";
    }

    @GetMapping("/nova")
    public String novaTarefa(Model model) {
        model.addAttribute("tarefa", new Tarefa());
        return "tarefas/formulario";
    }

    @PostMapping
    public String salvar(@Valid Tarefa tarefa, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "tarefas/formulario";
        }
        tarefaService.salvar(tarefa);
        return "redirect:/tarefas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("tarefa", tarefaService.buscarPorId(id));
        return "tarefas/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
        return "redirect:/tarefas";
    }
}