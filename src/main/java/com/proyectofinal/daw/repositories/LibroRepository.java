package com.proyectofinal.daw.repositories;

import java.util.Optional;

import com.proyectofinal.daw.entities.Libro;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findById(int id);
}
