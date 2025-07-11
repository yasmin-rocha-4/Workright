package com.seuprojeto.config;

import com.seuprojeto.model.Usuario;
import com.seuprojeto.security.UsuarioAutenticadoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioAutenticadoProvider usuarioAutenticadoProvider;

    @ModelAttribute("usuario")
    public Usuario adicionaUsuarioAutenticado() {
        try {
            return usuarioAutenticadoProvider.getUsuarioAutenticado();
        } catch (Exception e) {
            return null;
        }
    }

}
