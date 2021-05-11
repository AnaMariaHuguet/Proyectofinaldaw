package com.proyectofinal.daw.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Puntuacion;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.dto.UsuarioPerfilDTO;
import com.proyectofinal.daw.repositories.HistoricoRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import com.proyectofinal.daw.services.HistoricoService;
import com.proyectofinal.daw.services.PrestamoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PerfilController {

    private static final String PAG_PERFIL = "usuario/perfil";

    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    HistoricoRepository historicoRepo;

    @Autowired
    HistoricoService historicoService;
    @Autowired
    PrestamoService prestamoService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/perfil")
    public String perfil(Model model, HttpServletRequest request, @RequestParam Map<String, String> params) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        // Ver histórico
        String sortByhist = params.get("sortbyhist") != null ? params.get("sortbyhist").toString() : "id";
        String orderhist = params.get("orderhist") != null ? params.get("orderhist").toString() : "asc";

        Page<HistoricoPrestamos> pageHistorico = historicoService.findAll(params, usuario);

        int totalPageshist = pageHistorico.getTotalPages();
        if (totalPageshist > 0) {
            List<Integer> pageNumbershist = IntStream.rangeClosed(1, totalPageshist).boxed()
                    .collect(Collectors.toList());
            model.addAttribute("paginashist", pageNumbershist);
        }
        model.addAttribute("pagehistorico", pageHistorico.getContent());
        model.addAttribute("orderhist", orderhist);
        model.addAttribute("sortByhist", sortByhist);

        // Ver prestamos
        String sortByprest = params.get("sortbyprest") != null ? params.get("sortbyprest").toString() : "id";
        String orderprest = params.get("orderprest") != null ? params.get("orderprest").toString() : "asc";
        Page<Prestamo> pagePrestamo = prestamoService.findAll(params, usuario);
        int totalPagesprest = pagePrestamo.getTotalPages();
        if (totalPagesprest > 0) {
            List<Integer> pageNumbersprest = IntStream.rangeClosed(1, totalPagesprest).boxed()
                    .collect(Collectors.toList());
            model.addAttribute("paginasprest", pageNumbersprest);
        }
        model.addAttribute("pageprestamo", pagePrestamo.getContent());
        model.addAttribute("orderprest", orderprest);
        model.addAttribute("sortByprest", sortByprest);

        return PAG_PERFIL;
    }

    @PostMapping("/modificarPerfil")
    public String updateUser(@Valid @ModelAttribute("usuario") UsuarioPerfilDTO usuario, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return PAG_PERFIL;
        } else {
            // con los datos del usuario creamos un duplicado en formato DTO para realizar
            // los cambios
            // y lo guardamos en la BD
            Usuario user = usuarioRepo.getOne(usuario.getId());
            user.setNombre(
                    usuario.getNombre().substring(0, 1).toUpperCase() + usuario.getNombre().substring(1).toLowerCase());
            user.setApellido(usuario.getApellido().substring(0, 1).toUpperCase()
                    + usuario.getApellido().substring(1).toLowerCase());
            user.setNif(usuario.getNif());
            user.setAnoNac(usuario.getAnoNac());
            user.setDireccion(usuario.getDireccion().substring(0, 1).toUpperCase()
                    + usuario.getDireccion().substring(1).toLowerCase());
            user.setPoblacion(usuario.getPoblacion().substring(0, 1).toUpperCase()
                    + usuario.getPoblacion().substring(1).toLowerCase());
            user.setCod_postal(usuario.getCod_postal());
            user.setTelefono(usuario.getTelefono());
            user.setEmail(usuario.getEmail());

            if (!usuario.getContrasenya().equals("")) {
                user.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
            }
            usuarioRepo.save(user);
        }
        return PAG_PERFIL;
    }

}
