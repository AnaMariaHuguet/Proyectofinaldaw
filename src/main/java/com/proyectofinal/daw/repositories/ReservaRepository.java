package com.proyectofinal.daw.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.Usuario;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuario(Usuario usuario);
}
