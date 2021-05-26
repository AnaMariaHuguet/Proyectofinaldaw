package com.proyectofinal.daw.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.ClubLectura;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.dto.ClubLecturaDTO;
import com.proyectofinal.daw.repositories.ClubLecturaRepository;
import com.proyectofinal.daw.repositories.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminClubController {

    @Autowired
    ClubLecturaRepository repoCL;

    @Autowired
    LibroRepository repoLibro;

    @GetMapping("/admin/club")
    public String club(Model model, HttpServletRequest request) {
        List<ClubLectura> lista = repoCL.findAll();
        model.addAttribute("lectura", lista.get(lista.size() - 1));
        return "admin/adminClub";
    }

    @PostMapping("/libro/lecturaClub/{id}")
    public String chooseLibro(@PathVariable Long id, Model model, HttpServletRequest request) {
        Optional<Libro> libro = repoLibro.findById(id);
        if (libro.isPresent()) {
            ClubLectura clubLectura = new ClubLectura();
            clubLectura.setLibro(libro.get());
            clubLectura.setFechaCreacion(new Date());
            clubLectura.setFechaUltimaModificacion(new Date());
            repoCL.save(clubLectura);
            model.addAttribute("lectura", libro.get());
        }
        return club(model, request);
    }

    @PostMapping("/admin/club/editar")
    public String clubEditar(@ModelAttribute("clubDTO") ClubLecturaDTO clubDTO, Model model,
            HttpServletRequest request) {
        List<ClubLectura> clubs = repoCL.findAll();
        ClubLectura club = clubs.get(clubs.size() - 1);
        club.setEspacioURL(clubDTO.getEspacioURL());
        club.setFechaUltimaModificacion(new Date());
        repoCL.save(club);
        return club(model, request);
    }

}