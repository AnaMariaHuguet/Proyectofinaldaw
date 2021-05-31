package com.proyectofinal.daw.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findLibroByCategoria(Categoria categoria);

    List<Libro> findLibroByAutor(Autor autor);

    Optional<Libro> findLibroByTitulo(Long id);

    @Query("SELECT l FROM Libro l fetch all properties INNER JOIN l.autor a INNER JOIN l.categoria c INNER JOIN c.genero g WHERE g.id=?1 ")
    List<Libro> findLibroByGenero(Long id);

    @Modifying
    @Query("UPDATE Libro l SET l.libroSituacion = 'DISPONIBLE' WHERE l.id IN (SELECT l.id FROM Libro l INNER JOIN l.reserva r WHERE r.fDevolucion = ?1)")
    void setLibroInfoByfDevolucion(Date fDevolucion);
}
