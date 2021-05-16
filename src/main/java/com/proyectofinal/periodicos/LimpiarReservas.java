package com.proyectofinal.periodicos;

import java.util.Date;

import javax.transaction.Transactional;

import com.proyectofinal.daw.repositories.LibroRepository;
import com.proyectofinal.daw.repositories.ReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LimpiarReservas {

    @Autowired
    ReservaRepository repoReserva;

    @Autowired
    LibroRepository repoLibro;

    @Transactional
    public void limpiar() {
        repoLibro.setLibroInfoByfDevolucion(new Date());
        repoReserva.deleteAllByfDevolucion(new Date());
    }
}
