package com.proyectofinal.daw.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.proyectofinal.daw.enums.LibroSituacion;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Table(name = "Libros")
public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9\u00f1\u00d1\u00E0-\u00FC\s]{1,50}$", message = "Error al introducir el titulo")
    private String titulo;
    @Column
    private String isbn;
    private String editorial;
    private int ano;
    private String ubicacion;
    private String imagen;
    private String sinopsis;
    @Enumerated(EnumType.STRING)
    private LibroSituacion libroSituacion;
    @ManyToOne()
    @JoinColumn(name = "autor_id", nullable = false)
    @JsonManagedReference
    private Autor autor;
    @ManyToOne()
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonManagedReference
    private Categoria categoria;
    @OneToOne(mappedBy = "libro")
    @JsonIgnoreProperties("libro")
    private Reserva reserva;

    public Reserva getReserva() {
        return this.reserva;
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

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Autor getAutor() {
        return this.autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public LibroSituacion getLibroSituacion() {
        return this.libroSituacion;
    }

    public void setLibroSituacion(LibroSituacion libroSituacion) {
        this.libroSituacion = libroSituacion;
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

    public String getUbicacion() {
        return this.ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAno() {
        return this.ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

}
