package com.proyectofinal.daw.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CategoriaDTO {

    @Pattern(regexp = "^[a-zA-Z\s]{3,20}$", message = "Error al introducir el nombre")
    @NotBlank(message = "Nombre obligatorio")
    private String nombre;
    private Long genero;

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getGenero() {
        return this.genero;
    }

    public void setGenero(Long genero) {
        this.genero = genero;
    }
}
