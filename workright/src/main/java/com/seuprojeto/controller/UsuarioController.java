package com.seuprojeto.controller;

import com.seuprojeto.model.Usuario;
import com.seuprojeto.security.UsuarioDetails;
import com.seuprojeto.repository.UsuarioRepository;
import com.seuprojeto.security.UsuarioAutenticadoProvider;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             UsuarioAutenticadoProvider usuarioAutenticadoProvider,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioAutenticadoProvider = usuarioAutenticadoProvider;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para exibir o formulário de edição do usuário
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario autenticado = usuarioAutenticadoProvider.getUsuarioAutenticado();

        // Permite que o usuário edite apenas seu próprio perfil
        if (!autenticado.getId().equals(id)) {
            return "redirect:/tarefas";
        }

        model.addAttribute("usuario", autenticado);
        return "usuarios/formulario";
    }

    // Método para salvar as alterações feitas no perfil
   @PostMapping("/editar/{id}")
    public String salvar(@PathVariable Long id,
                        @ModelAttribute("usuario") Usuario usuarioForm,
                        BindingResult result) {

        if (result.hasErrors()) {
            return "usuarios/formulario";
        }

        Usuario autenticado = usuarioAutenticadoProvider.getUsuarioAutenticado();

        if (!autenticado.getId().equals(id)) {
            return "redirect:/tarefas";
        }

        Usuario usuarioOriginal = usuarioRepository.findById(id).orElseThrow();

        usuarioOriginal.setNome(usuarioForm.getNome());
        usuarioOriginal.setEmail(usuarioForm.getEmail());

        // Só altera a senha se veio preenchida
        if (usuarioForm.getSenha() != null && !usuarioForm.getSenha().isBlank()) {
            usuarioOriginal.setSenha(passwordEncoder.encode(usuarioForm.getSenha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioOriginal);

        // Reautentica o usuário com os dados atualizados
        UsuarioDetails usuarioDetails = new UsuarioDetails(usuarioAtualizado);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(usuarioDetails, null, usuarioDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);


        return "redirect:/tarefas";
    }


}
