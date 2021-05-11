package com.proyectofinal.daw.entities.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

//DTO= Data transfer object
public class UsuarioPerfilDTO {

    private Long id;
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{3,20}$", message = "Error al introducir el nombre")
    @NotBlank(message = "Nombre obligatorio")
    private String nombre;

    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\u00f1\u00d1\u00E0-\u00FC\s]{1,20}$", message = "Error al introducir el apellido")
    private String apellido;

    @NotBlank(message = "NIF obligatorio")
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "Error en el nif")
    private String nif;

    @Digits(fraction = 0, integer = 4, message = "Error al introducir el año")
    private int anoNac;
    @NotBlank(message = "direccion obligatorio")
    private String direccion;
    @Pattern(regexp = "^[a-zA-Z\s]{1,20}$", message = "Error al introducir la poblacion")
    @NotBlank(message = "poblacion obligatorio")
    private String poblacion;

    @Digits(fraction = 0, integer = 5, message = "Error al introducir el codigo postal")
    private int cod_postal;
    @Range(min = 600000000, max = 999999999, message = "Error al introducir el telefono")
    private int telefono;

    @NotBlank(message = "Email obligatorio")
    @Email
    private String email;
    private String contrasenya;

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

    public String getContrasenya() {
        return this.contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

}
