package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Puntuacion;
import com.proyectofinal.daw.entities.Reserva;
import com.proyectofinal.daw.entities.dto.LibroDTO;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.PuntuacionRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;
import com.proyectofinal.daw.services.LibroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminLibroController {

    @Autowired
    LibroRepository repoLibro;
    @Autowired
    PrestamoRepository repoPrestamo;
    @Autowired
    HistoricoRepository repoHistorico;
    @Autowired
    AutorRepository repoAutor;
    @Autowired
    CategoriaRepository repoCategoria;
    @Autowired
    GeneroRepository repoGenero;
    @Autowired
    ReservaRepository repoReserva;
    @Autowired
    PuntuacionRepository repoPuntuacion;
    @Autowired
    LibroService libroService;

    @GetMapping("admin/libro")
    public String libro(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {
        int currentPage = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        int pageSize = params.get("size") != null ? Integer.valueOf(params.get("size").toString()) : 6;
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Libro> pageLibro = libroService.pagTodosLibros(pageRequest);

        int totalPages = pageLibro.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        model.addAttribute("libro", repoLibro.findAll());
        model.addAttribute("libro", pageLibro.getContent());
        model.addAttribute("order", order);
        model.addAttribute("sortBy", sortBy);

        return "admin/adminLibro";
    }

    @GetMapping("/newLibro")
    public String nuevoLibro(Model model, HttpServletRequest request) {
        model.addAttribute("autores", repoAutor.findAll());
        model.addAttribute("categorias", repoCategoria.findAll());
        model.addAttribute("generos", repoGenero.findAll());
        model.addAttribute("libro", new Libro());
        return "/admin/adminNuevoLibro";
    }

    @PostMapping("/crearlibro")
    /* @RequestMapping(value = "/newGenero", method =RequestMethod.POST) */
    public String createLibro(@Valid @ModelAttribute("libroDTO") LibroDTO libroDTO, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            libroService.addLibro(libroDTO, model);
        }
        model.addAttribute("libros", repoLibro.findAll());
        return "/admin/adminNuevoLibro";
    }

    @PostMapping("/libro/borrar/{id}")
    public String borrarLibro(@PathVariable Long id, Model model) {

        Optional<Libro> libro = repoLibro.findById(id);
        Optional<Prestamo> listaPrestamos = repoPrestamo.findByLibro(libro.get());
        List<Reserva> listaReservas = repoReserva.findByLibro(libro.get());
        if (!listaPrestamos.isEmpty()) {
            model.addAttribute("errorserver", "No se puede borrar, esta prestado.");

        } else if (!listaReservas.isEmpty()) {
            model.addAttribute("errorserver", "No se puede borrar, esta reservado.");

        } else
            repoLibro.deleteById(id);

        model.addAttribute("libros", repoLibro.findAll());
        model.addAttribute("libro", new Libro());
        return "/admin/adminLibro";
    }

    @GetMapping("/editaLibro/{id}")
    public String editalibro(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        Optional<Libro> libro = repoLibro.findById(id);
        Optional<Prestamo> prestamo = repoPrestamo.findByLibro(libro.get());
        if (!libro.isPresent()) {
            model.addAttribute("errorserver", "Libro no encontrado");
            return null;
        } else {
            // el libro optional lo convierto en libro
            Libro book = libro.get();
            List<HistoricoPrestamos> historicoLibro = repoHistorico.findByLibro(book);
            int votacion = 0;
            int sumaVotacion = 0;
            int cantidadVotacion = 0;
            for (int i = 0; i < historicoLibro.size(); i++) {
                if (historicoLibro.get(i).getVotacion() != null) {
                    sumaVotacion = votacion + (historicoLibro.get(i).getVotacion().getPuntuacion().getValor());
                    cantidadVotacion++;
                }
            }
            int totalVotacion = 0;
            if (cantidadVotacion > 0) {
                totalVotacion = Math.round(sumaVotacion / cantidadVotacion);
            }
            Optional<Puntuacion> stringPuntuacion = repoPuntuacion.findByValor(sumaVotacion);
            String strPuntuacion = stringPuntuacion.isPresent() ? stringPuntuacion.get().getDescripcion()
                    : "No tiene votos";
            model.addAttribute("stringPuntuacion", strPuntuacion);
            model.addAttribute("cantidadVotacion", cantidadVotacion);
            model.addAttribute("totalVotacion", totalVotacion);
            model.addAttribute("libro", libro.get());
            if (prestamo.isPresent()) {
                model.addAttribute("prestamo", prestamo.get());
            }

            model.addAttribute("autores", repoAutor.findAll());
            model.addAttribute("categorias", repoCategoria.findAll());
            model.addAttribute("generos", repoGenero.findAll());
            // model.addAttribute("librosituacion", LibroSituacion.values());
            return "admin/editarLibroAdmin";

        }
    }
}
