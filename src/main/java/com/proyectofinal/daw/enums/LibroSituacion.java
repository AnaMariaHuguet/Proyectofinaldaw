package com.proyectofinal.daw.enums;

public enum LibroSituacion {
    DISPONIBLE, PRESTADO, RESERVADO;

    String text;

    public String getName() {
        return text;
    }
}
