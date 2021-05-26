package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.services.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    ReservaService reservaService;

    @GetMapping("/administracion")
    public String paginaAdmin() {
        return "admin/admin";
    }

    @GetMapping("/admin/todasReservas")
    public String verTodasReservas(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {

        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        Page<Reserva> pageReserva = reservaService.findAll(params);

        int totalPages = pageReserva.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        if (!pageReserva.isEmpty()) {
            model.addAttribute("reserva", pageReserva.getContent());
            model.addAttribute("order", order);
            model.addAttribute("sortBy", sortBy);
        } else {
            model.addAttribute("errorserver", "No hay ninguna reserva");
        }
        return "admin/adminReserva";
    }

}
