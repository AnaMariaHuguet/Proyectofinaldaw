package com.proyectofinal.daw.entities.dto;

public class LibroDTOconID extends LibroDTO {
    private Long id;
    private String libroSituacion;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibroSituacion() {
        return this.libroSituacion;
    }

    public void setLibroSituacion(String libroSituacion) {
        this.libroSituacion = libroSituacion;
    }

}