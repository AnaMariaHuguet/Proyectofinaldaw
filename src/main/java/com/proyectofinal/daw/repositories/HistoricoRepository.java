package com.proyectofinal.daw.repositories;

import java.util.List;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoPrestamos, Long> {

    Page<HistoricoPrestamos> findAllByUsuario(Usuario usuario, Pageable pageable);

    List<HistoricoPrestamos> findByLibro(Libro libro);

    List<HistoricoPrestamos> findByUsuario(Usuario usuario);

}
