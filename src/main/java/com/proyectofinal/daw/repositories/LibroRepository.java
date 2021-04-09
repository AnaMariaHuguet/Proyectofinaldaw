package com.proyectofinal.daw.repositories;

import java.util.List;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("SELECT l FROM Libro l INNER JOIN l.categoria c INNER JOIN c.genero g WHERE g.id=?1 and c.id=?2 ")
    List<Libro> findLibroByFiltrar1(Long idgenero, Long idcat);

    @Query("SELECT l FROM Libro l INNER JOIN l.categoria c INNER JOIN c.genero g INNER JOIN l.autor a WHERE g.id=?1 and c.id=?2 and a.id=?3")
    List<Libro> findLibroByFiltrar2(Long idgenero, Long idautor);

    @Query("SELECT l FROM Libro l INNER JOIN l.categoria c  INNER JOIN l.autor a WHERE  c.id=?1 and a.id=?2")
    List<Libro> findLibroByFiltrar3(Long idcat, Long idautor);

}
