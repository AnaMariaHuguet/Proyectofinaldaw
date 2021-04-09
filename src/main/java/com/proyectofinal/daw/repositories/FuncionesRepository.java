package com.proyectofinal.daw.repositories;

import java.util.Optional;

import com.proyectofinal.daw.entities.Funciones;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionesRepository extends JpaRepository<Funciones, Long> {

    Funciones getFuncionesByNombre(String nombre);
}
