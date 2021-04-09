package com.proyectofinal.daw.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VotacionClavePrincipal implements Serializable {

    @Column(name = "libro_id")
    private Long libro;

    @Column(name = "usuario_id")
    private Long usuario;

    public Long getLibro() {
        return this.libro;
    }

    public void setLibro(Long libro) {
        this.libro = libro;
    }

    public Long getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

}
