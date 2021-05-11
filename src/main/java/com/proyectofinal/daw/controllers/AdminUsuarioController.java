package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.PrestamoRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import com.proyectofinal.daw.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AdminUsuarioController {
    @Autowired
    UsuarioRepository repoUsuario;
    @Autowired
    PrestamoRepository repoPrestamo;
    @Autowired
    HistoricoRepository repoHistorico;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("admin/usuario")
    public String usuario(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        Page<Usuario> pageUsuario = usuarioService.findAll(params);
        int totalPages = pageUsuario.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", pageNumbers);
        }
        model.addAttribute("usuariostodos", pageUsuario.getContent());
        model.addAttribute("order", order);
        model.addAttribute("paginaactual", pageUsuario.getNumber());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("usuarios", repoUsuario.findAll());
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuariostodos", repoUsuario.findAll());
        return "admin/adminUsuario";
    }

    @GetMapping("/admin/perfilUsuario/{id}")
    public String perfilUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = repoUsuario.findById(id);
        List<HistoricoPrestamos> usuarioHist = repoHistorico.findByUsuario(usuario.get());
        List<Prestamo> usuarioPrest = repoPrestamo.findByUsuario(usuario.get());

        model.addAttribute("usuario", usuario.get());
        model.addAttribute("pagehistorico", usuarioHist);
        model.addAttribute("pageprestamo", usuarioPrest);
        return "admin/adminUsuarioPerfil";
    }

}
