package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminLibroController {

    @Autowired
    LibroRepository repoLibro;

    @Autowired
    PrestamoRepository repoPrestamo;

    @Autowired
    ReservaRepository repoReserva;

    @PostMapping("/newLibro")
    // @RequestMapping(value = "/newGenero", method = RequestMethod.POST)
    public String createGenero(@Valid @ModelAttribute("libro") Libro libro, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("libros", repoLibro.findAll());
            return "/admin/adminLibro";
        } else
        // hay que mirar que no este ya incluido el libro en la bd
        if (!repoLibro.findById(libro.getId()).isPresent()) {
            model.addAttribute("errorserver", "Este libro ya esta en la BD.");
        } else {
            repoLibro.save(libro);
        }
        model.addAttribute("generostodos", repoLibro.findAll());
        return "/admin/adminGenero";

    }

    @PostMapping("/libro/borrar/{id}")
    public String borrarLibro(@PathVariable Long id, Model model) {

        Optional<Libro> libro = repoLibro.findById(id);
        List<Prestamo> listaPrestamos = repoPrestamo.findByLibro(libro.get());
        List<Reserva> listaReservas = repoReserva.findByLibro(libro.get());
        if (!listaPrestamos.isEmpty()) {
            model.addAttribute("errorserver", "No se puede borrar, esta prestado.");

        } else if (!listaReservas.isEmpty()) {
            model.addAttribute("errorserver", "No se puede borrar, esta reservado.");

        } else
            repoLibro.deleteById(id);

        model.addAttribute("libros", repoLibro.findAll());
        model.addAttribute("libro", new Libro());
        return "/admin/adminLibro";
    }
}
