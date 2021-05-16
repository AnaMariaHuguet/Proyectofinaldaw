package com.proyectofinal.daw.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.dto.PrestamoNuevoDTO;
import com.proyectofinal.daw.enums.LibroSituacion;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
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

    public boolean nuevoPrestamo(PrestamoNuevoDTO prestamoDTO, Model model) {
        boolean prestado = false;
        // ver que la peticion de prestamo es valida, que no este prestado ya el libro
        Optional<Libro> libro = repoLibro.findById(prestamoDTO.getLibro());
        Optional<Usuario> usuario = repoUsuario.findById(prestamoDTO.getUsuario());
        Optional<Prestamo> prestamo = repoPrestamo.findByLibro(libro.get());
        if (prestamo.isPresent()) {
            model.addAttribute("errorserver", "Este libro ya esta prestado");
            prestado = true;
        } else {
            // guardar en BD prestamo
            Prestamo prestam = new Prestamo();
            prestam.setLibro(libro.get());
            prestam.setUsuario(usuario.get());
            prestam.setFPrestamo(prestamoDTO.getFPrestamo());
            prestam.setFDevolEstimada(prestamoDTO.getFDevolEstimada());
            repoPrestamo.save(prestam);
            // guardar en BD historico-prestamo
            HistoricoPrestamos historic = new HistoricoPrestamos();
            historic.setLibro(libro.get());
            historic.setUsuario(usuario.get());
            historic.setFPrestamo(prestamoDTO.getFPrestamo());
            repoHistorico.save(historic);
            // SituacionLibro cambiar a prestado
            libro.get().setLibroSituacion(LibroSituacion.PRESTADO);
        }
        return prestado;
    }
}
