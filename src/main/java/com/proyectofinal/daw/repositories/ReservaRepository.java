package com.proyectofinal.daw.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuario(Usuario usuario);

    Optional<Reserva> findByLibro(Optional<Libro> libro);

    void deleteAllByfDevolucion(Date fDevolucion);

}
