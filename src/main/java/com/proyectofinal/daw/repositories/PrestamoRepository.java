package com.proyectofinal.daw.repositories;

import java.util.List;

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

    List<Prestamo> findByLibro(Libro libro);

}
