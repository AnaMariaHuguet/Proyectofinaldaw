package com.proyectofinal.daw.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.proyectofinal.daw.entities.Funciones;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.entities.dto.UsuarioLogin;
import com.proyectofinal.daw.repositories.FuncionesRepository;
import com.proyectofinal.daw.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistroController {

    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    FuncionesRepository funcionesRepo;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String registro(Usuario usuario) {

        return "formulario/registro";
    }

    @PostMapping(value = "/form/registro")
    public String visualizarRegistro(@ModelAttribute Usuario usuarioFormulario, Model model) {
        return "formulario/registro";
    }

    @PostMapping(value = "/registro", params = "btnVolverLogin")
    public String registroALogin(Model model) {
        model.addAttribute("usuario", new UsuarioLogin());
        return "formulario/login";
    }

    @RequestMapping(value = "/registro", method = RequestMethod.POST)
    public String createUser(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
        usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
        List<Funciones> nuevasFunciones = new ArrayList<Funciones>();
        nuevasFunciones.add(funcionesRepo.getFuncionesByNombre("ROLE_VER_PAGINAS"));
        nuevasFunciones.add(funcionesRepo.getFuncionesByNombre("ROLE_RESERVAR_LIBRO"));
        usuario.setFunciones(nuevasFunciones);

        if (result.hasErrors()) {
            return "formulario/registro";
        } else {
            usuarioRepo.save(usuario);
        }
        return "redirect:/";
    }
}
