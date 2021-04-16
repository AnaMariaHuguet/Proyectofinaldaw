package com.proyectofinal.daw.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.proyectofinal.daw.entities.Autor;
import com.proyectofinal.daw.entities.Funciones;
import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.UsuarioLogin;
import com.proyectofinal.daw.repositories.AutorRepository;
import com.proyectofinal.daw.repositories.CategoriaRepository;
import com.proyectofinal.daw.repositories.FuncionesRepository;
import com.proyectofinal.daw.repositories.GeneroRepository;
import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import com.proyectofinal.daw.services.LibroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    FuncionesRepository funcionesRepo;

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

    @GetMapping("/registro")
    public String registro(Usuario usuario) {

        return "formulario/registro";
    }

    /*
     * @PostMapping(value = "/form/registro", params = "btnEntrar") public String
     * permisoVisitante() {
     * 
     * return "index"; }
     */

    /* @PostMapping(value = "/form/registro", params = "btnRegistro") */
    @PostMapping(value = "/form/registro")
    public String visualizarRegistro(@ModelAttribute Usuario usuarioFormulario, Model model) {
        return "formulario/registro";
    }

    @PostMapping(value = "/registro", params = "btnVolverLogin")
    public String registroALogin(Model model) {
        return login(model);
    }

    // @PostMapping("/registro")
    @RequestMapping(value = "/registro", method = RequestMethod.POST)
    public String createUser(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
        usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
        List<Funciones> nuevasFunciones = new ArrayList<Funciones>();
        nuevasFunciones.add(funcionesRepo.getFuncionesByNombre("FUNC_VER_PAGINAS"));
        usuario.setFunciones(nuevasFunciones);

        if (result.hasErrors()) {
            return "formulario/registro";
        } else {
            usuarioRepo.save(usuario);

        }
        return "redirect:/";
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam Map<String, Object> params, Model model) {
        int currentPage = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        int pageSize = params.get("size") != null ? Integer.valueOf(params.get("size").toString()) : 5;
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);

        Page<Libro> pageLibro = libroServicio.pagTodosLibros(pageRequest);

        int totalPages = pageLibro.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        model.addAttribute("libros", pageLibro.getContent());
        model.addAttribute("autores", repoAutor.findAll());
        model.addAttribute("categorias", repoCategoria.findAll());
        model.addAttribute("generos", repoGenero.findAll());

        return "libros/catalogo";
    }

    @GetMapping("/club")
    public String club() {
        return "libros/club";
    }

    @GetMapping("/noticias")
    public String noticias() {
        return "libros/noticias";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "libros/carrito";
    }
    /*
     * @PostMapping("/reserva") public String reserva(Libro libro) { Libro libro =
     * repoLibro.findById(id).get(); Optional<Libro> libro = repoLibro.findById(id);
     * if(libro.getLibroSituacion().equals("PRESTADO"){ params.situacion="prestado";
     * } else if(libro.getLibroSituacion().equals("RESERVADO"){
     * params.situacion="reservado"; }else{ libro.setLibroSituacion("reservado");
     * params.situacion="libre"; } return "catalogo"; }
     */

    @GetMapping("/libros/mostrarlibro/{id}")
    public String index(@PathVariable int id) {
        return "index";
    }

}