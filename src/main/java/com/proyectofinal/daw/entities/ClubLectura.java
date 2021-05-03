package com.proyectofinal.daw.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ClubLectura")
public class ClubLectura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne()
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaModificacion;
    private String espacioURL;
    @OneToMany(mappedBy = "clubLectura", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<ComentariosClub> comentarios;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Libro getLibro() {
        return this.libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Date getFechaUltimaModificacion() {
        return this.fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public String getEspacioURL() {
        return this.espacioURL;
    }

    public void setEspacioURL(String espacioURL) {
        this.espacioURL = espacioURL;
    }

    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<ComentariosClub> getComentarios() {
        return this.comentarios;
    }

    public void setComentarios(List<ComentariosClub> comentarios) {
        this.comentarios = comentarios;
    }

}
