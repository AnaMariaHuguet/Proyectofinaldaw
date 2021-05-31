package com.proyectofinal.daw.controllers;

import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.enums.LibroSituacion;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class UsuarioApiController {
    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    PrestamoRepository repoPrestamo;
    @Autowired
    ReservaRepository repoReserva;
    @Autowired
    LibroRepository repoLibro;

    /*
     * @GetMapping("/usuarios") public List<Usuario> getUsuario() { return
     * usuarioRepo.findAll(); }
     * 
     * @GetMapping("/usuarios/{id}") public ResponseEntity<Usuario>
     * getUsuario(@PathVariable Long id) { Optional<Usuario> usuario =
     * usuarioRepo.findById(id); if (usuario.isEmpty()) { throw new
     * ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario Not Found"); } return
     * new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK); }
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id, Model model) {
        // antes de borrar deberia verificar que no tiene libros en prestamo
        // ni reservados
        Optional<Usuario> usuario = usuarioRepo.findById(id);
        List<Prestamo> listaPrestamos = repoPrestamo.findByUsuario(usuario.get());
        List<Reserva> listaReservas = repoReserva.findByUsuario(usuario.get());
        if (!listaPrestamos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tiene préstamos en marcha.");
        } else if (!listaReservas.isEmpty()) {
            for (int i = 0; i < listaReservas.size(); i++) {
                Libro libroDisp = listaReservas.get(i).getLibro();
                libroDisp.setLibroSituacion(LibroSituacion.DISPONIBLE);
                repoLibro.save(libroDisp);
                repoReserva.deleteById(listaReservas.get(i).getId());
            }
        }
        usuarioRepo.deleteById(id);
        return new ResponseEntity<String>("Borrado con éxito", HttpStatus.OK);

    }

}
