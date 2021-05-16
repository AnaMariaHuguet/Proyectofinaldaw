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

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.services.AutorService;

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
public class AdminAutorController {

    @Autowired
    AutorRepository repoAutor;
    @Autowired
    AutorService autorService;

    @GetMapping("admin/autor")
    public String autor(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        Page<Autor> pageAutor = autorService.findAll(params);
        int totalPages = pageAutor.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        model.addAttribute("autorestodos", pageAutor.getContent());
        model.addAttribute("autores", repoAutor.findAll());
        model.addAttribute("order", order);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("autor", new Autor());
        return "admin/adminAutor";
    }

    @PostMapping("/newAutor")
    public String createAutor(@Valid @ModelAttribute("autor") Autor autor, BindingResult result, Model model,
            HttpServletRequest request) {

        if (!result.hasErrors()) {
            Optional<Autor> autorencontrado = repoAutor.findByNombreAndApellido(autor.getNombre(), autor.getApellido());
            if (!autorencontrado.isPresent()) {
                autor.setNombre(
                        autor.getNombre().substring(0, 1).toUpperCase() + autor.getNombre().substring(1).toLowerCase());
                autor.setApellido(autor.getApellido().substring(0, 1).toUpperCase()
                        + autor.getApellido().substring(1).toLowerCase());

                repoAutor.save(autor);
            } else {
                model.addAttribute("errorserver", "Ya existe ese autor");
            }
        }
        Map<String, String> params = new HashMap<>();
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        Page<Autor> pageAutor = autorService.findAll(params);
        int totalPages = pageAutor.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        model.addAttribute("autorestodos", pageAutor.getContent());
        model.addAttribute("autores", repoAutor.findAll());
        model.addAttribute("order", order);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("autor", new Autor());
        return "admin/adminAutor";
    }

    @PostMapping("/autor/borrar/{id}")
    public String borrarAutor(@PathVariable Long id, Model model, HttpServletRequest request) {

        // antes de borrar verificar que no tiene libros
        Optional<Autor> autor = repoAutor.findById(id);
        if (!autor.get().getLibros().isEmpty()) {
            model.addAttribute("errorserver", "No se puede borrar, tiene libros.");
        } else {
            repoAutor.deleteById(id);
        }
        Map<String, String> params = new HashMap<>();
        return autor(model, request, params);
    }

    @PostMapping("/autor/editar")
    public String updateAutor(@RequestParam Map<String, String> body, Model model, HttpServletRequest request) {
        Optional<Autor> autor = repoAutor.findById(Long.parseLong(body.get("id")));
        Map<String, String> params = new HashMap<>();

        if (!Pattern.matches("^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{3,30}$", body.get("nombre"))) {
            model.addAttribute("errorserver", "Error al introducir el nombre");
            return autor(model, request, params);
        } else if (!Pattern.matches("^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{3,30}$", body.get("apellido"))) {
            model.addAttribute("errorserver", "Error al introducir el apellido");
            return autor(model, request, params);
        } else {
            autor.get().setNombre(body.get("nombre"));
            autor.get().setApellido(body.get("apellido"));

            autor.get().setNombre(autor.get().getNombre().substring(0, 1).toUpperCase()
                    + autor.get().getNombre().substring(1).toLowerCase());
            autor.get().setApellido(autor.get().getApellido().substring(0, 1).toUpperCase()
                    + autor.get().getApellido().substring(1).toLowerCase());

            repoAutor.save(autor.get());

            return autor(model, request, params);
        }
    }
}
