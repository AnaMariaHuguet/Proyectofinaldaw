package com.proyectofinal.daw.controllers;

import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepo;

    @GetMapping("/usuarios")
    public List<Usuario> getUsuario() {
        return usuarioRepo.findAll();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepo.findById(id);
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario Not Found");
        }
        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }

    // POST -> Añadir
    @PostMapping("/usuarios")
    public Usuario add(@RequestBody Usuario newUser) {
        return usuarioRepo.save(newUser);
    }

    @PutMapping("/usuarios")
    public Usuario update(@RequestBody Usuario user) {
        return usuarioRepo.findById(user.getId()).map(usuar -> {
            usuar.setNombre(user.getNombre());
            usuar.setApellido(user.getApellido());
            usuar.setDireccion(user.getDireccion());
            usuar.setCod_postal(user.getCod_postal());
            usuar.setPoblacion(user.getPoblacion());
            usuar.setEmail(user.getEmail());
            usuar.setTelefono(user.getTelefono());
            usuar.setNif(user.getNif());
            usuar.setAnoNac(user.getAnoNac());
            usuar.setContrasenya(user.getContrasenya());

            return usuarioRepo.save(usuar);
        }).orElseGet(() -> {
            return usuarioRepo.save(user);
        });
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id) {
        // antes de borrar deberia verificar que no tiene libros
        usuarioRepo.deleteById(id);
        return new ResponseEntity<String>("borrado", HttpStatus.OK);
    }
}
