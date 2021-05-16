package com.proyectofinal.daw.entities.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LibroDTO {
    @NotBlank(message = "Título obligatorio")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9\u00f1\u00d1\u00E0-\u00FC\s]{1,20}$", message = "Error al introducir el titulo")
    private String titulo;
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9\u00f1\u00d1\u00E0-\u00FC\s]{1,20}$", message = "Error al introducir el Isbn")
    private String isbn;
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9\u00f1\u00d1\u00E0-\u00FC\s]{1,20}$", message = "Error al introducir la editorial")
    private String editorial;
    @Digits(fraction = 0, integer = 4, message = "Error al introducir el año")
    private int ano;
    private String ubicacion;
    private String imagen;
    private String sinopsis;
    private Long autor;
    private Long categoria;

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditorial() {
        return this.editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAno() {
        return this.ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getSinopsis() {
        return this.sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Long getAutor() {
        return this.autor;
    }

    public void setAutor(Long autor) {
        this.autor = autor;
    }

    public Long getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

}
