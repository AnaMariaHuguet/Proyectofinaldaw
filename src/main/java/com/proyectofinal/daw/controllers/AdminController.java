package com.proyectofinal.daw.controllers;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Genero;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import com.proyectofinal.daw.services.AutorService;
import com.proyectofinal.daw.services.CategoriaService;
import com.proyectofinal.daw.services.GeneroService;
import com.proyectofinal.daw.services.LibroService;
import com.proyectofinal.daw.services.PrestamoService;
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
    PrestamoRepository repoPrestamo;

    @Autowired
    ReservaService reservaService;
    @Autowired
    PrestamoService prestamoService;
    @Autowired
    GeneroService generoService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    AutorService autorService;
    @Autowired
    LibroService libroService;

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

    @GetMapping("admin/club")
    public String club(Model model, HttpServletRequest request) {
        model.addAttribute("libro", repoLibro.findAll());
        return "admin/adminClub";
    }

}
