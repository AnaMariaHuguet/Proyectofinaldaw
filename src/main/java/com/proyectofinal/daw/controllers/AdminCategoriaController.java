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

import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Genero;
import com.proyectofinal.daw.entities.dto.CategoriaDTO;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.services.CategoriaService;

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
public class AdminCategoriaController {

    @Autowired
    CategoriaRepository repoCategoria;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    GeneroRepository repoGenero;

    @GetMapping("admin/categoria")
    public String categoria(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        Page<Categoria> pageCategoria = categoriaService.findAll(params);
        int totalPages = pageCategoria.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        model.addAttribute("categoriastodas", pageCategoria.getContent());
        model.addAttribute("order", order);
        model.addAttribute("paginaactual", pageCategoria.getNumber());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("categorias", repoCategoria.findAll());
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("generostodos", repoGenero.findAll());
        return "admin/adminCategoria";
    }

    @PostMapping("/newcategoria")
    public String createCategoria(@Valid @ModelAttribute("categoriaDTO") CategoriaDTO categoriaDTO,
            BindingResult result, Model model, HttpServletRequest request) {

        if (!result.hasErrors()) {
            categoriaService.addCategoria(categoriaDTO);
        }
        Map<String, String> params = new HashMap<>();
        return categoria(model, request, params);

    }

    @PostMapping("/categoria/borrar/{id}")
    public String borrarCategoria(@PathVariable Long id, Model model, HttpServletRequest request) {

        // antes de borrar verificar que no tiene libros
        Optional<Categoria> categoria = repoCategoria.findById(id);
        if (!categoria.get().getLibros().isEmpty()) {
            model.addAttribute("errorserver", "No se puede borrar, tiene libros.");
        } else {
            repoCategoria.deleteById(id);
        }
        Map<String, String> params = new HashMap<>();
        return categoria(model, request, params);

    }

    @PostMapping("/categoria/editar")
    public String updateCategoria(@RequestParam Map<String, String> params, Model model, HttpServletRequest request) {
        Optional<Categoria> categoria = repoCategoria.findById(Long.parseLong(params.get("id")));

        if (!Pattern.matches("^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{3,30}$", params.get("nombre"))) {
            model.addAttribute("errorserver", "Error al introducir el nombre");

        } else {
            categoria.get().setNombre(params.get("nombre"));

            categoria.get().setNombre(categoria.get().getNombre().substring(0, 1).toUpperCase()
                    + categoria.get().getNombre().substring(1).toLowerCase());
            Genero genero = repoGenero.findByNombre(params.get("genero")).get();
            categoria.get().setGenero(genero);

            repoCategoria.save(categoria.get());
        }
        return categoria(model, request, params);
    }
}