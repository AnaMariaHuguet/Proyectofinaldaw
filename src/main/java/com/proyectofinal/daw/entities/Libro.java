package com.proyectofinal.daw.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.proyectofinal.daw.enums.LibroSituacion;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Table(name = "Libros")
public class Libro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titulo;
    @Column
    private String isbn;
    private String editorial;
    private int ano;
    private String ubicacion;
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
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Votacion> votaciones;
    @JoinColumn(name = "libro_id")
    @OneToOne()
    private Reserva reserva;

    public Reserva getReserva() {
        return this.reserva;
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

    public List<Votacion> getVotaciones() {
        return this.votaciones;
    }

    public void setVotaciones(List<Votacion> votaciones) {
        this.votaciones = votaciones;
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
