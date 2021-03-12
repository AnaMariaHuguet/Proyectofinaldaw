package com.proyectofinal.daw.controllers;


import java.util.List;

import com.proyectofinal.daw.entities.Libro;
import com.proyectofinal.daw.repositories.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ApiController implements BaseApiController{
    
    @Autowired
    LibroRepository repoLibro;

    @GetMapping("/todoLibros")
    @ResponseBody       
    public List<Libro> getLibros() {       
       
        List<Libro> todosLibros = (List<Libro>) repoLibro.findAll();
        System.out.println("Han entrado en todos los libros");
        return todosLibros;
    }

    @GetMapping("/anadirLibros")
    @ResponseBody       
    public String addLibros() {       
        
        Libro newLibro = new Libro ();
        newLibro.setTitulo("DonQuijote2");
        newLibro.setAno(1971);
        repoLibro.save(newLibro);
        
        return "Añadir";
    }

}
