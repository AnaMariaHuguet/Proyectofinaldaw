package com.proyectofinal.daw.controllers;

import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FormController {

    @Autowired
    UsuarioRepository usuarioRepo;

}
