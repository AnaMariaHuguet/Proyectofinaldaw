package com.proyectofinal.daw.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.ReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {
    @Autowired
    ReservaRepository repoReserva;

    @GetMapping("/usuario/reserva")
    public String reservas(Model model, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Reserva> reservaUsuario = repoReserva.findByUsuario(usuario);
        model.addAttribute("reserva", reservaUsuario);
        return "usuario/reserva";
    }
}
