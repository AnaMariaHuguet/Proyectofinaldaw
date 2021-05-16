package com.proyectofinal.daw.services;

import java.util.Optional;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.dto.LibroDTO;
import com.proyectofinal.daw.enums.LibroSituacion;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/* service es una especie de DAO*/
@Service
public class LibroService {

    @Autowired
    LibroRepository repoLibro;
    @Autowired
    AutorRepository repoAutor;
    @Autowired
    CategoriaRepository repoCategoria;

    public Page<Libro> pagTodosLibros(Pageable pageable) {

        return repoLibro.findAll(pageable);
    }

    public void addLibro(LibroDTO lib, Model model) {

        Optional<Autor> autore = repoAutor.findById(lib.getAutor());
        Autor autor = autore.get();
        Optional<Categoria> categorie = repoCategoria.findById(lib.getCategoria());
        Categoria categoria = categorie.get();
        // String image = !lib.getImagen().isEmpty() ? lib.getImagen():
        // "help-books1.jpg";
        /* guardar en un libro */
        Libro libro = new Libro();
        libro.setTitulo(lib.getTitulo());
        libro.setIsbn(lib.getIsbn());
        libro.setAno(lib.getAno());
        libro.setUbicacion(lib.getUbicacion());
        libro.setEditorial(lib.getEditorial());
        libro.setSinopsis(lib.getSinopsis());
        libro.setAutor(autor);
        libro.setCategoria(categoria);
        libro.setLibroSituacion(LibroSituacion.DISPONIBLE);

        // libro.setImagen(image);
        model.addAttribute("errorserver", "Fallo al guardar el libro.");

    }
}
