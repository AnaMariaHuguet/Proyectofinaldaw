package com.proyectofinal.daw.controllers;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.dto.PrestamoNuevoDTO;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import com.proyectofinal.daw.services.PrestamoService;
import com.proyectofinal.daw.enums.LibroSituacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminPrestamoController {
    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    LibroRepository repoLibro;
    @Autowired
    PrestamoRepository repoPrestamo;
    @Autowired
    HistoricoRepository repoHistorico;
    @Autowired
    PrestamoService prestamoService;

    @GetMapping("/admin/prestamo")
    public String verPrestamos(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {

        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        Page<Prestamo> pagePrestamo = prestamoService.findAll(params);

        int totalPages = pagePrestamo.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        if (!pagePrestamo.isEmpty()) {
            model.addAttribute("prestamo", pagePrestamo.getContent());
            model.addAttribute("order", order);
            model.addAttribute("sortBy", sortBy);
        } else {
            model.addAttribute("errorserver", "No hay ningún préstamo");
        }

        return "admin/adminPrestamo";
    }

    @GetMapping("/admin/prestamo/nuevo")

    public String prestamoNuevo(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        model.addAttribute("libros", repoLibro.findAll());
        model.addAttribute("localDateTime", LocalDateTime.now());
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime devolucionEstimada = today.plusDays(15);
        model.addAttribute("devolucionEstimada", devolucionEstimada);
        model.addAttribute("prestamo", repoPrestamo.findAll());

        return "admin/adminPrestamoNuevo";
    }

    @PostMapping("/newPrestamo")
    public String createPrestamo(@ModelAttribute("prestamoNuevoDTO") PrestamoNuevoDTO prestamoNuevoDTO, Model model,
            HttpServletRequest request) {

        prestamoService.nuevoPrestamo(prestamoNuevoDTO, model);

        Map<String, String> params = new HashMap<>();
        return prestamoNuevo(model, request, params);
    }

    @PostMapping("/alerta")
    public String crearAlerta(Model model, HttpServletRequest request) {
        Usuario usuar = (Usuario) request.getSession().getAttribute("usuario");

        prestamoService.avisoVencimiento(usuar, model);
        return "index";
    }

    @PostMapping("/prestamo/borrar/{id}")
    // devolución prestamo
    public String borrarPrestamo(@PathVariable Long id, Model model, HttpServletRequest request) {
        /* enviarlo a historico */
        Optional<Prestamo> prestamo = repoPrestamo.findById(id);
        Libro libro = prestamo.get().getLibro();
        HistoricoPrestamos historico = new HistoricoPrestamos();
        historico.setFPrestamo(prestamo.get().getFPrestamo());
        historico.setLibro(libro);
        historico.setUsuario(prestamo.get().getUsuario());
        historico.setFDevolucion(new Date());
        repoHistorico.save(historico);
        // borrarlo de prestamo
        repoPrestamo.deleteById(id);
        // poner el libro en situacionLibro DISPONIBLE
        libro.setLibroSituacion(LibroSituacion.DISPONIBLE);
        repoLibro.save(libro);

        Map<String, String> params = new HashMap<>();
        return verPrestamos(model, request, params);
    }
}
