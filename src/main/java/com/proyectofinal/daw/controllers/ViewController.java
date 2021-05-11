package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Puntuacion;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.dto.UsuarioLogin;
import com.proyectofinal.daw.enums.LibroSituacion;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.repositories.CategoriaRepository;

import com.proyectofinal.daw.repositories.FuncionesRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.PuntuacionRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import com.proyectofinal.daw.services.LibroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    LibroRepository repoLibro;
    @Autowired
    LibroService libroServicio;
    @Autowired
    AutorRepository repoAutor;
    @Autowired
    CategoriaRepository repoCategoria;
    @Autowired
    GeneroRepository repoGenero;
    @Autowired
    ReservaRepository repoReserva;
    @Autowired
    FuncionesRepository funcionesRepo;
    @Autowired
    HistoricoRepository repoHistorico;
    @Autowired
    PuntuacionRepository repoPuntuacion;
    // @Autowired
    // LibroSituacionRepository repoLibroSituacion;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("usuario", new UsuarioLogin());
        return "formulario/login";
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuario = usuarioRepo.findByEmail(email);
        if (usuario.isPresent()) {
            request.getSession().setAttribute("nombre", usuario.get().getNombre());
            request.getSession().setAttribute("usuario", usuario.get());
            // request.getSession().setAttribute("rol", usuario.get().getRoleName());
        }
        return "index";
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam Map<String, Object> params, Model model) {
        int currentPage = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        int pageSize = params.get("size") != null ? Integer.valueOf(params.get("size").toString()) : 6;
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<Libro> pageLibro = libroServicio.pagTodosLibros(pageRequest);

        int totalPages = pageLibro.getTotalPages();
        if (totalPages > 0) {
            /*
             * El método boxed () de la clase IntStream devuelve un Stream que consta de los
             * elementos de este flujo, cada uno encuadrado en un Entero. De igual forma que
             * podemos convertir el Stream a un array con el método toArray() , podemos usar
             * los Java Stream Collectors y convertir nuestro stream a una Lista o Conjunto
             * , utilizando la clase Collectors y su método toList() o toSet().
             */
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        model.addAttribute("libros", pageLibro.getContent());
        model.addAttribute("autores", repoAutor.findAll());
        model.addAttribute("categorias", repoCategoria.findAll());
        model.addAttribute("generos", repoGenero.findAll());
        model.addAttribute("order", order);
        model.addAttribute("sortBy", sortBy);
        return "libros/catalogo";
    }

    @GetMapping("/noticias")
    public String noticias() {
        return "libros/noticias";
    }

    @GetMapping("/libro/{id}")
    public String libro(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response, Model model) {
        Optional<Libro> libro = repoLibro.findById(id);
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

            if (request.isUserInRole("ADMINISTRADOR")) {
                model.addAttribute("autores", repoAutor.findAll());
                model.addAttribute("categorias", repoCategoria.findAll());
                model.addAttribute("generos", repoGenero.findAll());
                model.addAttribute("librosituacion", LibroSituacion.values());
                return "admin/editarLibroAdmin";
            } else {
                return "libros/libro";
            }

        }
    }
}