package com.proyectofinal.daw.repositories;

import java.util.List;
import java.util.Optional;

import com.proyectofinal.daw.entities.Autor;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Optional<Autor> findByNombre();
    Optional<Autor> findByNombre(String nombre);

    // List<Autor> findByApellido(String apellido);
    // @Query("SELECT nombre FROM Autor")
    // List<String> buscaNombreAll();

}
