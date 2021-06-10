package com.proyectofinal.daw.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties(value = { "votaciones" })
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{3,20}$", message = "Error al introducir el nombre")
    @NotBlank(message = "Nombre obligatorio")
    private String nombre;
    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{1,20}$", message = "Error al introducir el apellido")
    private String apellido;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "NIF obligatorio")
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "Error en el nif")
    private String nif;
    @Column(length = 4)
    @Digits(fraction = 0, integer = 4, message = "Error al introducir el año")
    private int anoNac;
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9-,\u00f1\u00d1\u00E0-\u00FC\s]{1,30}$", message = "Error al introducir la dirección")
    @NotBlank(message = "direccion obligatorio")
    private String direccion;
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9-,\u00f1\u00d1\u00E0-\u00FC\s]{1,30}$", message = "Error al introducir la población")
    @NotBlank(message = "poblacion obligatorio")
    private String poblacion;
    @Column(length = 5, name = "`codigo postal`")
    @Digits(fraction = 0, integer = 5, message = "Error al introducir el codigo postal")
    private int cod_postal;
    @Range(min = 600000000, max = 999999999, message = "Error al introducir el telefono")
    private int telefono;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email obligatorio")
    @Email
    private String email;
    @Column(nullable = false)
    @NotBlank(message = "Contraseña obligatoria")
    private String contrasenya;
    // @ManyToMany(mappedBy = "usuarios", fetch = FetchType.EAGER)
    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinTable(name = "funciones_usuarios", joinColumns = { @JoinColumn(name = "usuario_id") }, inverseJoinColumns = {
            @JoinColumn(name = "funciones_id") })
    private List<Funciones> funciones;
    @OneToMany(mappedBy = "usuario")
    @JsonBackReference
    private List<ComentariosClub> comentariosClub;

    public List<Funciones> getFunciones() {
        return this.funciones;
    }

    public void setFunciones(List<Funciones> funciones) {
        this.funciones = funciones;
    }

    public String getContrasenya() {
        return this.contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
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

    public String getNif() {
        return this.nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public int getAnoNac() {
        return this.anoNac;
    }

    public void setAnoNac(int anoNac) {
        this.anoNac = anoNac;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPoblacion() {
        return this.poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public int getCod_postal() {
        return this.cod_postal;
    }

    public void setCod_postal(int cod_postal) {
        this.cod_postal = cod_postal;
    }

    public int getTelefono() {
        return this.telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ComentariosClub> getComentariosClub() {
        return this.comentariosClub;
    }

    public void setComentariosClub(List<ComentariosClub> comentariosClub) {
        this.comentariosClub = comentariosClub;
    }

    public void addFuncion(Funciones fun) {
        funciones.add(fun);
        fun.getUsuarios().add(this);
    }

    public void removeFuncion(Funciones fun) {

        for (int i = 0; i < funciones.size(); i++) {
            if (funciones.get(i).getId().equals(fun.getId())) {
                funciones.remove(i);
            }
        }
        fun.getUsuarios().remove(this);
    }
}
