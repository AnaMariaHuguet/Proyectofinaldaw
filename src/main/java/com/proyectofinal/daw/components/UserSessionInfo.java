package com.proyectofinal.daw.components;

import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.context.annotation.ScopedProxyMode;

@Component
@Scope(scopeName="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionInfo {

    private Usuario usuario;

    @Autowired
    UsuarioRepository usuarioRepo;

    public Usuario getUsuario() {
        if (usuario == null) {
            try {
                usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String email = usuario.getEmail();
                usuario = usuarioRepo.findByEmail(email).get();
            } catch (Exception e) {
                usuario = null;
            }
        }
        return usuario;
    }
}