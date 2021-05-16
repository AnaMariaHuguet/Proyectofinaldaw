package com.proyectofinal.daw.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.dto.PrestamoNuevoDTO;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import com.proyectofinal.daw.services.PrestamoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminPrestamoController {
    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    LibroRepository repoLibro;
    @Autowired
    PrestamoRepository repoPrestamo;
    @Autowired
    PrestamoService prestamoService;

    @GetMapping("/admin/prestamo")
    public String verPrestamos(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {

        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        Page<Prestamo> pagePrestamo = prestamoService.findAll(params);

        int totalPages = pagePrestamo.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        if (!pagePrestamo.isEmpty()) {
            model.addAttribute("prestamo", pagePrestamo.getContent());
            model.addAttribute("order", order);
            model.addAttribute("sortBy", sortBy);
        } else {
            model.addAttribute("errorserver", "No hay ningún préstamo");
        }
        return "admin/adminPrestamo";
    }

    @GetMapping("/admin/prestamo/nuevo")

    public String prestamoNuevo(Model model, HttpServletRequest request) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        model.addAttribute("libros", repoLibro.findAll());
        model.addAttribute("localDateTime", LocalDateTime.now());
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime devolucionEstimada = today.plusDays(15);
        model.addAttribute("devolucionEstimada", devolucionEstimada);
        model.addAttribute("prestamo", repoPrestamo.findAll());
        return "admin/adminPrestamoNuevo";
    }

    @PostMapping("/newPrestamo")
    public String createPrestamo(@ModelAttribute("prestamo") PrestamoNuevoDTO prestamoDTO, Model model,
            HttpServletRequest request) {

        boolean hecho = prestamoService.nuevoPrestamo(prestamoDTO, model);
        if (hecho) {
            model.addAttribute("errorserver", "Libro prestado correctamente");
        }
        return "admin/adminPrestamoNuevo";
    }

}
