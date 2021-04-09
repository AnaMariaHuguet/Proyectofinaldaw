package com.proyectofinal.daw.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "puntuacion")
public class Puntuacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int valor;
    @OneToMany(mappedBy = "puntuacion", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Votacion> votaciones;

    public List<Votacion> getVotaciones() {
        return this.votaciones;
    }

    public void setVotaciones(List<Votacion> votaciones) {
        this.votaciones = votaciones;
    }

    public int getValor() {
        return this.valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
