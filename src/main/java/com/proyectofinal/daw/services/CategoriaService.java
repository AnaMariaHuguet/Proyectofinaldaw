package com.proyectofinal.daw.services;

import java.util.Map;
import java.util.Optional;

import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Genero;
import com.proyectofinal.daw.entities.dto.CategoriaDTO;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository repoCategoria;

    @Autowired
    GeneroRepository repoGenero;

    public void addCategoria(CategoriaDTO cat, Model model) {
        /*
         * categoriaDTo: nombre categoria y nombre genero elegidos por usuario, saco un
         * opcional objeto genero para incluirlo junto con el nombre de la categoria en
         * el nuevo objeto categoria
         */
        Optional<Genero> genero = repoGenero.findById(cat.getGenero());

        if (genero.isPresent()) {
            /* creo objeto categoria */
            Categoria categoria = new Categoria();
            categoria.setNombre(
                    cat.getNombre().substring(0, 1).toUpperCase() + cat.getNombre().substring(1).toLowerCase());
            categoria.setGenero(genero.get());
            if (!repoCategoria.findByNombre(categoria.getNombre()).isPresent()) {
                repoCategoria.save(categoria);
            } else {
                model.addAttribute("errorserver", "Ya existe esa categoria");
            }
        } else {
            model.addAttribute("errorserver", "No existe el género.");
        }
    }

    public Page<Categoria> findAll(Map<String, String> params) {
        int currentPage = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        int pageSize = params.get("size") != null ? Integer.valueOf(params.get("size").toString()) : 6;
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        return repoCategoria.findAll(pageRequest);
    }
}
