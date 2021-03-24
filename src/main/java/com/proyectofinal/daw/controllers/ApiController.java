package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.repositories.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    LibroRepository repoLibro;

    // GET => Recuperar datos
    @GetMapping("/libros")
    public List<Libro> getLibros() {
        return repoLibro.findAll();
    }

    @GetMapping("/libros/{id}")
    public ResponseEntity<Libro> getLibro(@PathVariable Long id) {
        Optional<Libro> libro = repoLibro.findById(id);
        if (libro.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro Not Found");
        }
        return new ResponseEntity<Libro>(libro.get(), HttpStatus.OK);
    }

    // POST -> Añadir
    @PostMapping("/libros")
    public Libro add(@RequestBody Libro newLibro) {
        return repoLibro.save(newLibro);
    }

    @PutMapping("/libros")
    public Libro update(@RequestBody Libro myLibro) {
        return repoLibro.findById(myLibro.getId()).map(libro -> {
            libro.setTitulo(myLibro.getTitulo());

            return repoLibro.save(libro);
        }).orElseGet(() -> {
            return repoLibro.save(myLibro);
        });
    }

    @DeleteMapping("/libros/{id}")
    public ResponseEntity<String> borrarLibros(@PathVariable Long id) {
        repoLibro.deleteById(id);
        return new ResponseEntity<String>("borrado", HttpStatus.OK);
    }

}
