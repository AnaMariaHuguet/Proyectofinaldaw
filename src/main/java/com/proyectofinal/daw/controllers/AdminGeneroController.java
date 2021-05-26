package com.proyectofinal.daw.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.proyectofinal.daw.entities.Genero;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.services.GeneroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminGeneroController {

    @Autowired
    GeneroRepository repoGenero;
    @Autowired
    GeneroService generoService;

    @GetMapping("admin/genero")
    public String genero(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        Page<Genero> pageGenero = generoService.findAll(params);
        int totalPages = pageGenero.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        model.addAttribute("generostodos", pageGenero.getContent());
        model.addAttribute("order", order);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("generos", repoGenero.findAll());
        model.addAttribute("genero", new Genero());
        return "admin/adminGenero";
    }

    @PostMapping("/newGenero")
    // @RequestMapping(value = "/newGenero", method = RequestMethod.POST)
    public String createGenero(@Valid @ModelAttribute("genero") Genero genero, BindingResult result, Model model,
            HttpServletRequest request) {

        if (result.hasErrors()) {
            model.addAttribute("generostodos", repoGenero.findAll());
            return "/admin/adminGenero";
        } else
        // antes de guardar ver que no esta ya en la bd
        if (!repoGenero.findByNombre(genero.getNombre()).isPresent()) {
            // especifico que guarde la primera en mayúsculas 
            genero.setNombre(
                    genero.getNombre().substring(0, 1).toUpperCase() );
            repoGenero.save(genero);
        } else {
            model.addAttribute("errorserver", "Ya existe ese género");
        }
        Map<String, String> params = new HashMap<>();
        return genero(model, request, params);
    }

    @PostMapping("/genero/borrar/{id}")
    public String borrarGenero(@PathVariable Long id, Model model, HttpServletRequest request) {
        // antes de borrar verificar que no tiene categorías
        Optional<Genero> genero1 = repoGenero.findById(id);
        if (!genero1.get().getCategorias().isEmpty()) {
            model.addAttribute("errorserver", "No se puede borrar, tiene categorías.");
        } else {
            repoGenero.deleteById(id);
        }
        Map<String, String> params = new HashMap<>();
        return genero(model, request, params);
    }

    @PostMapping("/genero/editar")
    public String updateGenero(@RequestParam Map<String, String> params, Model model, HttpServletRequest request) {
        Optional<Genero> genero1 = repoGenero.findById(Long.parseLong(params.get("id")));

        if (!Pattern.matches("^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{3,30}$", params.get("nombre"))) {
            model.addAttribute("errorserver", "Error al introducir el nombre");

        } else {
            genero1.get().setNombre(params.get("nombre"));
            if (!genero1.get().getNombre().isEmpty()) {
                genero1.get().setNombre(genero1.get().getNombre().substring(0, 1).toUpperCase()
                        + genero1.get().getNombre().substring(1).toLowerCase());
            }
            repoGenero.save(genero1.get());
        }
        return genero(model, request, params);
    }
}
