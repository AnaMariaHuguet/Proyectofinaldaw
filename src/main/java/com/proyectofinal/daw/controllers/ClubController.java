package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.ClubLectura;
import com.proyectofinal.daw.entities.Funciones;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.ClubLecturaRepository;
import com.proyectofinal.daw.repositories.FuncionesRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClubController {

    private static final String PAGINA_INSCRIPTOR = "libros/clubInscriptor";
    private static final String PAGINA_USUARIO = "libros/clubUsuario";

    @Autowired
    UsuarioRepository repoUsuario;

    @Autowired
    FuncionesRepository repoFunciones;
    @Autowired
    ClubLecturaRepository repoCL;

    @GetMapping("/club")
    public String club(HttpServletRequest request, Model model) {
        List<ClubLectura> lista = repoCL.findAll();
        model.addAttribute("lectura", lista.get(lista.size() - 1));
        if (request.isUserInRole("CLUB_LECTURA")) {
            return PAGINA_INSCRIPTOR;
        } else if (request.isUserInRole("VER_PAGINAS")) {
            return PAGINA_USUARIO;
        } else if (request.isUserInRole("SOLO_VISITANTE")) {
            return "libros/clubVisitante";
        } else {
            return "/";
        }
    }

    @PostMapping("/inscripcion")
    public String inscriptor(HttpServletRequest request, Model model) {
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
        List<ClubLectura> lista = repoCL.findAll();
        model.addAttribute("lectura", lista.get(lista.size() - 1));
        return PAGINA_INSCRIPTOR;
    }

    @PostMapping("/anularinscripcion")
    public String anularInscriptor(HttpServletRequest request, Model model) {
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
        List<ClubLectura> lista = repoCL.findAll();
        model.addAttribute("lectura", lista.get(lista.size() - 1));
        return PAGINA_USUARIO;
    }
}
