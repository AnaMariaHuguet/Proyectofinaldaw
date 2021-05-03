package com.proyectofinal.daw.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Prestamo")
@JsonIgnoreProperties("votaciones")
public class Prestamo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "usuario_id", nullable = false)
    @ManyToOne()
    private Usuario usuario;
    @OneToOne()
    @JoinColumn(name = "libro_id", nullable = false)
    @JsonIgnoreProperties({ "prestamo, votaciones" })
    private Libro libro;
    @Temporal(TemporalType.TIMESTAMP)
    private Date f_prestamo;
    @Temporal(TemporalType.DATE)
    private Date f_devolucion;
    @Temporal(TemporalType.DATE)
    private Date f_devol_Estimada;

    public Date getF_devol_Estimada() {
        return this.f_devol_Estimada;
    }

    public void setF_devol_Estimada(Date f_devol_Estimada) {
        this.f_devol_Estimada = f_devol_Estimada;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return this.libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Date getF_prestamo() {
        return this.f_prestamo;
    }

    public void setF_prestamo(Date f_prestamo) {
        this.f_prestamo = f_prestamo;
    }

    public Date getF_devolucion() {
        return this.f_devolucion;
    }

    public void setF_devolucion(Date f_devolucion) {
        this.f_devolucion = f_devolucion;
    }

}
