package com.proyectofinal.daw.repositories;

import java.util.List;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoPrestamos, Long> {

    List<HistoricoPrestamos> findByUsuario(Usuario usuario);

    List<HistoricoPrestamos> findByLibro(Libro libro);

}
