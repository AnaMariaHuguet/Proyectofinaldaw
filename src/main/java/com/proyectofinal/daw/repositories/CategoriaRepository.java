package com.proyectofinal.daw.repositories;

import java.util.List;
import java.util.Map;

import com.proyectofinal.daw.entities.Categoria;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
