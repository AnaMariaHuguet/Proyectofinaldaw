package com.proyectofinal.daw.entities;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "votacion")

public class Votacion implements Serializable {

    @EmbeddedId
    private VotacionClavePrincipal id;

    @MapsId("libro")
    @ManyToOne()
    @JoinColumn(name = "libro_id", nullable = false)
    @JsonBackReference
    private Libro libro;
    @MapsId("usuario")
    @ManyToOne()
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Usuario usuario;
    @ManyToOne()
    @JoinColumn(name = "puntuacion_id", nullable = false)
    @JsonBackReference
    private Puntuacion puntuacion;

    public VotacionClavePrincipal getId() {
        return this.id;
    }

    public void setId(VotacionClavePrincipal id) {
        this.id = id;
    }

    public Libro getLibro() {
        return this.libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Puntuacion getPuntuacion() {
        return this.puntuacion;
    }

    public void setPuntuacion(Puntuacion puntuacion) {
        this.puntuacion = puntuacion;
    }
}
