package com.proyectofinal.daw.services;

import java.util.Collections;
import java.util.List;

import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.repositories.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// service es una especie de DAO
@Service
public class LibroService {

    @Autowired
    LibroRepository repoLibro;

    public Page<Libro> pagTodosLibros(Pageable pageable) {

        return repoLibro.findAll(pageable);
    }
}
