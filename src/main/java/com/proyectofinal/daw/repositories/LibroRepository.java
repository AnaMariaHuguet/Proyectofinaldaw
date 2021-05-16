package com.proyectofinal.daw.repositories;

import java.util.Date;
import java.util.List;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    /*
     * @Query("SELECT categoria_id FROM Libro ") List<String> buscaCategorias();
     * 
     * @Query("SELECT  autor_id FROM Libro") List<String> buscaAutores();
     * 
     * 
     */

    List<Libro> findLibroByCategoria(Categoria categoria);

    List<Libro> findLibroByAutor(Autor autor);

    @Query("SELECT l FROM Libro l fetch all properties INNER JOIN l.autor a INNER JOIN l.categoria c INNER JOIN c.genero g WHERE g.id=?1 ")
    List<Libro> findLibroByGenero(Long id);

    @Query("SELECT l FROM Libro l INNER JOIN l.categoria c INNER JOIN c.genero g INNER JOIN l.autor a WHERE g.id=?1 and c.id=?2 and a.id=?3")
    List<Libro> findLibroByFiltrar(Long idgenero, Long idcat, Long idautor);

    @Modifying
    @Query("UPDATE Libro l SET l.libroSituacion = 'DISPONIBLE' WHERE l.id IN (SELECT l.id FROM Libro l INNER JOIN l.reserva r WHERE r.fDevolucion = ?1)")
    void setLibroInfoByfDevolucion(Date fDevolucion);
}
