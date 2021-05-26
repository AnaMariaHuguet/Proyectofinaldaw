package com.proyectofinal.daw.repositories;

import java.util.Optional;

import com.proyectofinal.daw.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNif(String nif);

}
