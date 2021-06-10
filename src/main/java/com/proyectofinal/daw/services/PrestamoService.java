package com.proyectofinal.daw.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.dto.PrestamoNuevoDTO;
import com.proyectofinal.daw.enums.LibroSituacion;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class PrestamoService {

    @Autowired
    PrestamoRepository repoPrestamo;
    @Autowired
    ReservaRepository repoReserva;
    @Autowired
    LibroRepository repoLibro;
    @Autowired
    UsuarioRepository repoUsuario;
    @Autowired
    HistoricoRepository repoHistorico;

    public Page<Prestamo> findAllByUsuario(Map<String, String> params, Usuario usuario) {
        int currentPage = params.get("pageprest") != null ? Integer.valueOf(params.get("pageprest").toString()) - 1 : 0;
        int pageSize = params.get("sizeprest") != null ? Integer.valueOf(params.get("sizeprest").toString()) : 6;
        String sortBy = params.get("sortbyprest") != null ? params.get("sortbyprest").toString() : "id";
        String order = params.get("orderprest") != null ? params.get("orderprest").toString() : "asc";
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        return repoPrestamo.findAllByUsuario(usuario, pageRequest);
    }

    public Page<Prestamo> findAll(Map<String, String> params) {
        int currentPage = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        int pageSize = params.get("size") != null ? Integer.valueOf(params.get("size").toString()) : 6;
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        return repoPrestamo.findAll(pageRequest);
    }

    public void nuevoPrestamo(PrestamoNuevoDTO prestamoDTO, Model model) {
        // ver que la peticion de prestamo es valida, que no este prestado ya el libro
        Optional<Libro> libro = repoLibro.findById(prestamoDTO.getLibro());
        Optional<Usuario> usuario = repoUsuario.findById(prestamoDTO.getUsuario());
        Optional<Prestamo> prestamo = repoPrestamo.findByLibro(libro.get());
        // miro cantidad de prestamos que tiene el usuario
        List<Prestamo> listaPrestUser = repoPrestamo.findByUsuario(usuario.get());
        // miro si ese libro ha sido reservado con anterioridad
        Optional<Reserva> reserva = repoReserva.findByLibro(libro);

        if (listaPrestUser.size() > 2) {
            model.addAttribute("errorserver", "Imposible realizar el préstamo.\n Máximo prestamos ya alcanzado");
        } else if (prestamo.isPresent()) {
            model.addAttribute("errorserver", "Este libro ya esta prestado");
        } else if (reserva.isPresent() && !(reserva.get().getUsuario()).equals(usuario.get())) {
            model.addAttribute("errorserver", "Este libro ya esta reservado por otro usuario");
        } else {
            System.out.println("guardar prestamo");
            // guardar en BD prestamo
            Prestamo prestam = new Prestamo();
            prestam.setLibro(libro.get());
            prestam.setUsuario(usuario.get());
            prestam.setFPrestamo(prestamoDTO.getFPrestamo());
            prestam.setFDevolEstimada(prestamoDTO.getFDevolEstimada());
            repoPrestamo.save(prestam);
            // SituacionLibro cambiar a prestado
            libro.get().setLibroSituacion(LibroSituacion.PRESTADO);
            repoLibro.save(libro.get());
            if (reserva.isPresent()) {
                // quitar de reserva
                repoReserva.delete(reserva.get());
            }
            model.addAttribute("infoserver", "Préstamo correcto.");
        }

    }

    public void avisoVencimiento(Usuario usuario, Model model) {
        List<Prestamo> lista = repoPrestamo.findByUsuario(usuario);
        if (!lista.isEmpty()) {
            LocalDate today = LocalDate.now();
            LocalDate margen3 = today.plusDays(3);
            LocalDate margen2 = today.plusDays(2);
            LocalDate margen1 = today.plusDays(1);

            for (int i = 0; i < lista.size(); i++) {
                LocalDate fechaDE = Instant.ofEpochMilli(lista.get(i).getFDevolEstimada().getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                if (fechaDE.equals(margen3)) {
                    model.addAttribute("infoserver", "En 3 días te expira el plazo de tu libro");
                } else if (fechaDE.equals(margen2)) {
                    model.addAttribute("infoserver", "En 2 días te expira el plazo de tu libro");
                } else if (fechaDE.equals(margen1)) {
                    model.addAttribute("infoserver", "En 1 día te expira el plazo de tu libro");
                } else if (fechaDE.equals(today)) {
                    model.addAttribute("infoserver", "Recuerda devolver tu libro");
                }
            }

        }
    }
}
