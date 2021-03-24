package com.proyectofinal.daw.controllers;

import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FormController {
    
    @Autowired
    UsuarioRepository usuarioRepo;

    
}
