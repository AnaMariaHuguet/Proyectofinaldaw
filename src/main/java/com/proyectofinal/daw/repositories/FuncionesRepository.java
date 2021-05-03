package com.proyectofinal.daw.repositories;

import java.util.List;
import java.util.Optional;

import com.proyectofinal.daw.entities.Funciones;
import com.proyectofinal.daw.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionesRepository extends JpaRepository<Funciones, Long> {

    Funciones getFuncionesByNombre(String nombre);

}
