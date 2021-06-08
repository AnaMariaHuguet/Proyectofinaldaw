package com.proyectofinal.daw.services;

import java.util.Optional;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.dto.LibroDTO;
import com.proyectofinal.daw.entities.dto.LibroDTOconID;
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
        try {
            Optional<Autor> autore = repoAutor.findById(lib.getAutor());
            Autor autor = autore.get();
            Optional<Categoria> categorie = repoCategoria.findById(lib.getCategoria());
            Categoria categoria = categorie.get();
            /* guardar en un libro */
            Libro libro = new Libro();
            libro.setTitulo(lib.getTitulo().substring(0, 1).toUpperCase() + lib.getTitulo().substring(1).toLowerCase());
            libro.setIsbn(lib.getIsbn());
            libro.setAno(lib.getAno());
            libro.setUbicacion(lib.getUbicacion());
            if (!lib.getEditorial().isEmpty()) {
                libro.setEditorial(lib.getEditorial().substring(0, 1).toUpperCase()
                        + lib.getEditorial().substring(1).toLowerCase());
            }
            if (!lib.getSinopsis().isEmpty()) {
                libro.setSinopsis(
                        lib.getSinopsis().substring(0, 1).toUpperCase() + lib.getSinopsis().substring(1).toLowerCase());
            }
            libro.setAutor(autor);
            libro.setCategoria(categoria);
            libro.setLibroSituacion(LibroSituacion.DISPONIBLE);
            // String image = !lib.getImagen().isEmpty() ? lib.getImagen():
            // "help-books1.jpg";
            // libro.setImagen(lib.getImagen());
            libro.setImagen("help-books1.jpg");
            repoLibro.save(libro);
            model.addAttribute("errorserver", "Libro guardado correctamente.");

        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorserver", "Fallo al guardar el libro.");

        }
    }

    public void updateLibro(LibroDTOconID lib, Model model) {
        try {
            Optional<Libro> libro = repoLibro.findById(lib.getId());
            Optional<Autor> autore = repoAutor.findById(lib.getAutor());
            Autor autor = autore.get();
            Optional<Categoria> categorie = repoCategoria.findById(lib.getCategoria());
            Categoria categoria = categorie.get();

            /* guardar en un libro */

            libro.get().setTitulo(
                    lib.getTitulo().substring(0, 1).toUpperCase() + lib.getTitulo().substring(1).toLowerCase());
            libro.get().setIsbn(lib.getIsbn());
            libro.get().setAno(lib.getAno());
            libro.get().setUbicacion(lib.getUbicacion());
            if (!lib.getEditorial().isEmpty()) {
                libro.get().setEditorial(lib.getEditorial().substring(0, 1).toUpperCase()
                        + lib.getEditorial().substring(1).toLowerCase());
            }
            if (!lib.getSinopsis().isEmpty()) {
                libro.get().setSinopsis(
                        lib.getSinopsis().substring(0, 1).toUpperCase() + lib.getSinopsis().substring(1).toLowerCase());
            }
            libro.get().setAutor(autor);
            libro.get().setCategoria(categoria);
            libro.get().setLibroSituacion(LibroSituacion.valueOf(lib.getLibroSituacion()));
            // libro.setImagen(lib.getImage());
            // String image = !lib.getImagen().isEmpty() ? lib.getImagen():
            // "help-books1.jpg";
            repoLibro.save(libro.get());

            model.addAttribute("errorserver", "Libro guardado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorserver", "Fallo al guardar el libro.");
        }

    }
}
