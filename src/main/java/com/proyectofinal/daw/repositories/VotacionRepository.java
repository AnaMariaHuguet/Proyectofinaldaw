package com.proyectofinal.daw.repositories;

import com.proyectofinal.daw.entities.Votacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotacionRepository extends JpaRepository<Votacion, Long> {

}
