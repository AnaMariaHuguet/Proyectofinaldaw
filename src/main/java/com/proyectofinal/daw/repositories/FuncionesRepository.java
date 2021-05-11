package com.proyectofinal.daw.repositories;

import com.proyectofinal.daw.entities.Funciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionesRepository extends JpaRepository<Funciones, Long> {

    Funciones getFuncionesByNombre(String nombre);

}
