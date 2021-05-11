package com.proyectofinal.daw.repositories;

import java.util.List;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuario(Usuario usuario);

    List<Reserva> findByLibro(Libro libro);

}
