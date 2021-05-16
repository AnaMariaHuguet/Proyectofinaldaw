package com.proyectofinal.daw.repositories;

import java.util.List;
import java.util.Optional;

import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Page<Prestamo> findAllByUsuario(Usuario usuario, Pageable pageable);

    List<Prestamo> findByUsuario(Usuario usuario);

    List<Prestamo> findAllByLibro(Libro libro);

    Optional<Prestamo> findByLibro(Libro libro);

}
