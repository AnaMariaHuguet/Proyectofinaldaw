package com.proyectofinal.daw.entities.dto;

import java.util.Date;

public class PrestamoNuevoDTO {
    // id del usuario
    private Long usuario;
    // id del libro
    private Long libro;
    private Date fPrestamo;
    private Date fDevolEstimada;

    public Long getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Long getLibro() {
        return this.libro;
    }

    public void setLibro(Long libro) {
        this.libro = libro;
    }

    public Date getFPrestamo() {
        return this.fPrestamo;
    }

    public void setFPrestamo(Date fPrestamo) {
        this.fPrestamo = fPrestamo;
    }

    public Date getFDevolEstimada() {
        return this.fDevolEstimada;
    }

    public void setFDevolEstimada(Date fDevolEstimada) {
        this.fDevolEstimada = fDevolEstimada;
    }

}
