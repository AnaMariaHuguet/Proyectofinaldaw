package com.proyectofinal.daw.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Genero;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.enums.LibroSituacion;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;

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

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    LibroRepository repoLibro;

    @Autowired
    CategoriaRepository repoCategoria;
    @Autowired
    GeneroRepository repoGenero;

    @Autowired
    AutorRepository repoAutor;

    @Autowired
    ReservaRepository repoReserva;

    @PostMapping("/libro/genero")
    public List<Libro> getLibrosPorGenero(@RequestBody Map<String, String> params) {
        Long id = Long.parseLong(params.get("genero"));
        return repoLibro.findLibroByGenero(id);
    }
    /*
     * @PostMapping("/libro/filtrar") public List<Libro>
     * getLibrosFiltrado(@RequestBody Map<String, String> params) { Long idgenero =
     * params.get("genero") != null ? Long.parseLong(params.get("genero")) : -1;
     * Long idcat = params.get("categoria") != null ?
     * Long.parseLong(params.get("categoria")) : -1; Long idautor =
     * params.get("autor") != null ? Long.parseLong(params.get("autor")) : -1; //
     * Long idcat = Long.parseLong(params.get("categoria")); // Long idautor =
     * Long.parseLong(params.get("autor")); if (idgenero != -1 && idcat != -1 &&
     * idautor != -1) { return repoLibro.findLibroByFiltrar(idgenero, idcat,
     * idautor); } if (idgenero != -1 && idcat != -1) { return
     * repoLibro.findLibroByFiltrar1(idgenero, idcat); } if (idgenero != -1 &&
     * idautor != -1) { return repoLibro.findLibroByFiltrar2(idgenero, idautor); }
     * if (idcat != -1 && idautor != -1) { return
     * repoLibro.findLibroByFiltrar3(idcat, idautor); } return
     * repoLibro.findLibroByFiltrar(idgenero, idcat, idautor); }
     */

    @PostMapping("/genero/id")
    public Genero getCategoriaporGeneros(@RequestBody Map<String, String> params) {
        long id = Long.parseLong(params.get("genero"));
        return repoGenero.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genero no encontrado"));
    }

    @PostMapping("/libro/categoria")
    public List<Libro> getLibrosPorCategoria(@RequestBody Map<String, String> params) {
        long id = Long.parseLong(params.get("categoria"));
        Categoria categoria = repoCategoria.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro Not Found"));

        return repoLibro.findLibroByCategoria(categoria);
    }

    @PostMapping("/libro/todas")
    public List<Categoria> gettodasCategoria() {
        return repoCategoria.findAll();
    }

    @PostMapping("/libro/autor")
    public List<Libro> getLibrosPorAutor(@RequestBody Map<String, String> params) {
        long id = Long.parseLong(params.get("autor"));
        Autor autor = repoAutor.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro Not Found"));

        return repoLibro.findLibroByAutor(autor);
    }

    // GET => Recuperar datos
    @GetMapping("/libros")
    public List<Libro> getLibros() {
        return repoLibro.findAll();
    }

    @GetMapping("/libros/{id}")
    public ResponseEntity<Libro> getLibro(@PathVariable Long id) {
        Optional<Libro> libro = repoLibro.findById(id);
        if (libro.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro Not Found");
        }
        return new ResponseEntity<Libro>(libro.get(), HttpStatus.OK);
    }

    // POST -> Añadir
    @PostMapping("/libros")
    public Libro add(@RequestBody Libro libro) {
        return repoLibro.save(libro);
    }

    @PutMapping("/libros")
    public Libro update(@RequestBody Libro myLibro) {
        return repoLibro.findById(myLibro.getId()).map(libro -> {
            libro.setTitulo(myLibro.getTitulo());
            return repoLibro.save(libro);
        }).orElseGet(() -> {
            return repoLibro.save(myLibro);
        });
    }

    @DeleteMapping("/libros/{id}")
    public ResponseEntity<String> borrarLibros(@PathVariable Long id) {
        repoLibro.deleteById(id);
        return new ResponseEntity<String>("borrado", HttpStatus.OK);
    }

    @PostMapping("/reserva")
    // ResponseEntity representa la respuesta HTTP completa: código de estado,
    // encabezados y cuerpo .
    public ResponseEntity<List<Reserva>> reservarLibro(@RequestBody Map<String, Long> params,
            HttpServletRequest request) {
        // saco de la sesion al usuario que esta haciendo la reserva
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        // busco libro a través del id que se quiere reservar
        Optional<Libro> libro = repoLibro.findById(params.get("id"));
        List<Reserva> reservaAnterior = new ArrayList<>();
        // si no hay libro o usuario que salga un error
        if (libro.isEmpty() || usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Libro o usuario Not Found");
        }
        // si el libro ya esta reservado que salga un error
        if (libro.get().getReserva() != null) {
            throw new ResponseStatusException(HttpStatus.GONE, "Libro ya reservado");
        }
        //
        List<Reserva> reservaUsuario = repoReserva.findByUsuario(usuario);
        for (Reserva reserva : reservaUsuario) {
            if (reserva.getF_devolucion() == null) {
                reservaAnterior.add(reserva);
            }
        }
        // creamos un nuevo objeto reserva, introducimos datos y guardamos en la BD
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setLibro(libro.get());
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setF_reservaHecha(new Date());
        nuevaReserva.setF_prestamo(new Date(new Date().getTime() + (1000 * 60 * 60 * 48)));
        repoReserva.save(nuevaReserva);
        // añadimos el nuevo objeto reserva a la arraylist
        reservaAnterior.add(nuevaReserva);
        // cambiamos situación del objeto libro
        libro.get().setLibroSituacion(LibroSituacion.RESERVADO);
        repoLibro.save(libro.get());
        return new ResponseEntity<List<Reserva>>(reservaAnterior, HttpStatus.OK);
    }

    @DeleteMapping("/reserva/{id}")
    public ResponseEntity<List<Reserva>> borrarReserva(@PathVariable Long id, HttpServletRequest request) {
        // sacamos el libro a traves del id del libro
        Libro libro = repoLibro.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro Not Found"));
        // con el libro que hemos obtenido sacamos la id de la reserva
        // borramos reserva
        repoReserva.deleteById(libro.getReserva().getId());
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Reserva> reservaUsuario = repoReserva.findByUsuario(usuario);
        return new ResponseEntity<List<Reserva>>(reservaUsuario, HttpStatus.OK);
    }
}
