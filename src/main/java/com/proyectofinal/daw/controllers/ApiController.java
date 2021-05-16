package com.proyectofinal.daw.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Categoria;
import com.proyectofinal.daw.entities.Genero;
import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Puntuacion;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.Votacion;
import com.proyectofinal.daw.enums.LibroSituacion;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.PuntuacionRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;
import com.proyectofinal.daw.repositories.VotacionRepository;

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
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    PrestamoRepository repoPrestamo;
    @Autowired
    HistoricoRepository repoHistorico;
    @Autowired
    VotacionRepository repoVotacion;
    @Autowired
    PuntuacionRepository repoPuntuacion;

    @PostMapping("/libro/genero")
    public List<Libro> getLibrosPorGenero(@RequestBody Map<String, String> params) {
        Long id = Long.parseLong(params.get("genero"));
        return repoLibro.findLibroByGenero(id);
    }

    @PostMapping("/genero/id")
    public Genero getCategoriaporGeneros(@RequestBody Map<String, String> params) {
        long id = Long.parseLong(params.get("genero"));
        return repoGenero.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Género no encontrado"));
    }

    @PostMapping("/libro/categoria")
    public List<Libro> getLibrosPorCategoria(@RequestBody Map<String, String> params) {
        long id = Long.parseLong(params.get("categoria"));
        Categoria categoria = repoCategoria.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

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
        // ver que no este repetido
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
        // antes de borrar mirar que no este prestado o reservado

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
        // busco el libro a través del id que se quiere reservar
        Optional<Libro> libro = repoLibro.findById(params.get("id"));
        List<Reserva> reservaAnterior = new ArrayList<>();
        // si no hay libro o usuario que salga un error
        if (libro.isEmpty() || usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Libro o usuario no encontrado");
        }
        // si el libro ya esta reservado que salga un error
        if (libro.get().getReserva() != null) {
            throw new ResponseStatusException(HttpStatus.GONE, "Libro ya reservado");
        }
        //
        List<Reserva> reservaUsuario = repoReserva.findByUsuario(usuario);

        if (reservaUsuario.size() > 2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ha superado el límite máximo de reservas");
        }
        for (Reserva reserva : reservaUsuario) {
            reservaAnterior.add(reserva);
        }
        // creamos un nuevo objeto reserva, introducimos datos y guardamos en la BD
        Reserva nuevaReserva = new Reserva();
        Libro lib = libro.get();
        nuevaReserva.setLibro(lib);
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setFReservaHecha(new Date());
        nuevaReserva.setFDevolucion(new Date(new Date().getTime() + (1000 * 60 * 60 * 48)));
        repoReserva.save(nuevaReserva);
        // añadimos el nuevo objeto reserva a la arraylist
        reservaAnterior.add(nuevaReserva);
        // cambiamos situación del objeto libro
        lib.setLibroSituacion(LibroSituacion.RESERVADO);
        repoLibro.save(lib);
        return new ResponseEntity<List<Reserva>>(reservaAnterior, HttpStatus.OK);
    }

    @DeleteMapping("/reserva/{id}")
    public ResponseEntity<List<Reserva>> borrarReserva(@PathVariable Long id, HttpServletRequest request) {
        // sacamos el libro a traves del id del libro
        Libro libro = repoLibro.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro Not Found"));
        // con el libro que hemos obtenido sacamos la id de la reserva
        // borramos reserva
        Reserva reserva = libro.getReserva();
        if (reserva != null) {
            repoReserva.deleteById(reserva.getId());
            // cambiar situación del libro en Libro
            libro.setLibroSituacion(LibroSituacion.DISPONIBLE);
            repoLibro.save(libro);
        }
        // Devolvemos la lista de reserva actualizada al usuario
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Reserva> reservaUsuario = repoReserva.findByUsuario(usuario);
        return new ResponseEntity<List<Reserva>>(reservaUsuario, HttpStatus.OK);
    }

    @PostMapping("/reserva/usuario")
    public ResponseEntity<List<Reserva>> reservarLibro(HttpServletRequest request) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Reserva> reservaUsuario = repoReserva.findByUsuario(usuario);

        return new ResponseEntity<List<Reserva>>(reservaUsuario, HttpStatus.OK);
    }

    @PostMapping("/votahistorico")
    public ResponseEntity<String> votar(@RequestBody Map<String, String> params) {
        long id = Long.parseLong(params.get("id"));
        int puntuacion = Integer.parseInt(params.get("puntuacion"));
        Optional<HistoricoPrestamos> historico = repoHistorico.findById(id);
        Optional<Puntuacion> puntuacionObj = repoPuntuacion.findByValor(puntuacion);
        Date hoy = new Date();
        Votacion votacion = historico.get().getVotacion();
        if (Objects.isNull(votacion)) {// if(votacion==null)
            votacion = new Votacion();
        }
        votacion.setPuntuacion(puntuacionObj.get());
        votacion.setFVotacion(hoy);
        votacion.setHistorico(historico.get());
        repoVotacion.save(votacion);
        historico.get().setVotacion(votacion);
        repoHistorico.save(historico.get());

        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }
}
