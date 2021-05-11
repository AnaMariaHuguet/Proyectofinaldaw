package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Optional;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.Funciones;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.FuncionesRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClubController {

    private static final String PAGINA_INSCRIPTOR = "libros/clubInscriptor";

    @Autowired
    UsuarioRepository repoUsuario;

    @Autowired
    FuncionesRepository repoFunciones;

    @GetMapping("/club")
    public String club(HttpServletRequest request) {
        if (request.isUserInRole("CLUB_LECTURA")) {
            return PAGINA_INSCRIPTOR;
        } else if (request.isUserInRole("VER_PAGINAS")) {
            return "libros/clubUsuario";
        } else if (request.isUserInRole("SOLO_VISITANTE")) {
            return "libros/clubVisitante";
        } else {
            return "/";
        }
    }

    @PostMapping("/inscripcion")
    public String inscriptor(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = repoUsuario.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // sacamos las funciones del usuario
            List<Funciones> funcionUsuario = usuario.getFunciones();
            // sacamos la funcion que deseamos introducir al usuario
            Funciones funcionClub = repoFunciones.getFuncionesByNombre("ROLE_CLUB_LECTURA");
            // añadimos la funcion a la lista del usuario
            funcionUsuario.add(funcionClub);
            // añadimos la lista de funciones al usuario
            usuario.setFunciones(funcionUsuario);
            // guardamos el usuario
            repoUsuario.save(usuario);
            request.getSession().removeAttribute("usuario");
            request.getSession().setAttribute("usuario", usuario);
        }
        return PAGINA_INSCRIPTOR;
    }

    @PostMapping("/anularinscripcion")
    public String anularInscriptor(HttpServletRequest request) {
        // Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        // List<Funciones> funcionUsuario = usuario.getFunciones();
        // sacamos la funcion que queremos borrar al usuario
        // funcionUsuario.remove(funcionClub);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuario = repoUsuario.findByEmail(email);
        if (usuario.isPresent()) {
            Usuario real = usuario.get();
            real.removeFuncion(repoFunciones.getFuncionesByNombre("ROLE_CLUB_LECTURA"));
            repoUsuario.save(real);
            request.getSession().removeAttribute("usuario");
            request.getSession().setAttribute("usuario", real);
        }
        return "libros/clubUsuario";
    }
}
