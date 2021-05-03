package com.proyectofinal.daw.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Puntuacion;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.dto.UsuarioPerfilDTO;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PerfilController {

    private static final String PAG_PERFIL = "usuario/perfil";

    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    HistoricoRepository historicoRepo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/perfil")
    public String perfil(Model model, HttpServletRequest request) {

        if (request.isUserInRole("SOLO_VISITANTE")) {
            return "/";
        }
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<HistoricoPrestamos> listahistoria = historicoRepo.findByUsuario(usuario);
        Puntuacion puntuacion = new Puntuacion();
        model.addAttribute("usuario", usuario);
        model.addAttribute("historico", listahistoria);
        model.addAttribute("puntuacion", puntuacion);
        return PAG_PERFIL;
    }

    @PostMapping("/modificarPerfil")
    public String updateUser(@Valid @ModelAttribute("usuario") UsuarioPerfilDTO usuario, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return PAG_PERFIL;
        } else {
            // con los datos del usuario creamos un duplicado en formato DTO para realizar
            // los cambios
            // y lo guardamos en la BD
            Usuario user = usuarioRepo.getOne(usuario.getId());
            user.setNombre(usuario.getNombre());
            user.setApellido(usuario.getApellido());
            user.setNif(usuario.getNif());
            user.setAnoNac(usuario.getAnoNac());
            user.setDireccion(usuario.getDireccion());
            user.setPoblacion(usuario.getPoblacion());
            user.setCod_postal(usuario.getCod_postal());
            user.setTelefono(usuario.getTelefono());
            user.setEmail(usuario.getEmail());

            if (!usuario.getContrasenya().equals("")) {
                user.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
            }

            usuarioRepo.save(user);
        }
        return PAG_PERFIL;
    }
}
