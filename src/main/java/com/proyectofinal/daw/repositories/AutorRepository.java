package com.proyectofinal.daw.repositories;

import java.util.Optional;

import com.proyectofinal.daw.entities.Autor;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.nombre=:nombre AND a.apellido=:apellido")
    public Optional<Autor> findByNombreAndApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

}
