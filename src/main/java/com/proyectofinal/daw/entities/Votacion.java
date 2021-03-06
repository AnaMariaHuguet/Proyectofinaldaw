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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "votacion")

public class Votacion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "votacion")
    @JsonBackReference
    private HistoricoPrestamos historico;
    @ManyToOne()
    @JoinColumn(name = "puntuacion_id", nullable = false)
    @JsonManagedReference
    private Puntuacion puntuacion;
    @Temporal(TemporalType.DATE)
    private Date fVotacion;

    public Puntuacion getPuntuacion() {
        return this.puntuacion;
    }

    public void setPuntuacion(Puntuacion puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HistoricoPrestamos getHistorico() {
        return this.historico;
    }

    public void setHistorico(HistoricoPrestamos historico) {
        this.historico = historico;
    }

    public Date getFVotacion() {
        return this.fVotacion;
    }

    public void setFVotacion(Date fVotacion) {
        this.fVotacion = fVotacion;
    }

}
