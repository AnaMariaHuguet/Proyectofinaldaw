package com.proyectofinal.daw.controllers;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import com.proyectofinal.daw.services.ReservaService;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AdminController {
    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    LibroRepository repoLibro;
    @Autowired
    ReservaRepository repoReserva;
    @Autowired
    GeneroRepository repoGenero;
    @Autowired
    CategoriaRepository repoCategoria;
    @Autowired
    AutorRepository repoAutor;

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

        model.addAttribute("reserva", pageReserva.getContent());
        model.addAttribute("order", order);
        model.addAttribute("sortBy", sortBy);
        return "admin/adminReserva";
    }

    @GetMapping("admin/genero")
    public String genero(Model model, HttpServletRequest request) {
        model.addAttribute("genero", repoGenero.findAll());
        return "admin/adminGenero";
    }

    @GetMapping("admin/categoria")
    public String categoria(Model model, HttpServletRequest request) {
        model.addAttribute("categoria", repoCategoria.findAll());
        return "admin/adminCategoria";
    }

    @GetMapping("admin/autor")
    public String autor(Model model, HttpServletRequest request) {
        model.addAttribute("autor", repoAutor.findAll());
        return "admin/adminAutor";
    }

    @GetMapping("admin/libro")
    public String libro(Model model, HttpServletRequest request) {
        model.addAttribute("libro", repoLibro.findAll());
        return "admin/editarLibroAdmin";
    }
}
