package com.proyectofinal.daw.repositories;

import com.proyectofinal.daw.entities.Libro;

import org.springframework.data.repository.CrudRepository;

public interface LibroRepository extends CrudRepository <Libro, Long> {
    
}
