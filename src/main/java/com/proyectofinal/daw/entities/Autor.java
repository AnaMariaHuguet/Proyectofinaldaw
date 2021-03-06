package com.proyectofinal.daw.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@Table(name = "autores")
@Indexed
public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{3,30}$", message = "Error al introducir el nombre")
    @NotBlank(message = "Nombre obligatorio")
    @FullTextField
    private String nombre;
    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{3,30}$", message = "Error al introducir el apellido")
    @FullTextField
    private String apellido;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonBackReference
    private List<Libro> libros;

    public List<Libro> getLibros() {
        return this.libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
