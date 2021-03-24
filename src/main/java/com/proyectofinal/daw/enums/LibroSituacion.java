package com.proyectofinal.daw.enums;

public enum LibroSituacion {
    LIBRE, PRESTADO, RESERVADO;

    String text;

    public String getName() {
        return text;
    }
}
