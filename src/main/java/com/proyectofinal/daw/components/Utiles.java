package com.proyectofinal.daw.components;

public class Utiles {

    public static boolean esDniValido(String dni) {
        String dniChars = "TRWAGMYFPDXBNJZSQVHLCKE";
        boolean isNumeric;
        boolean esValido = true;

        String intPartDNI = dni.trim().replaceAll(" ", "").substring(0, 7);
        char ltrDNI = dni.charAt(8);

        int valNumDni = Integer.parseInt(intPartDNI) % 23;
        isNumeric = intPartDNI.matches("[+-]?\\d*(\\.\\d+)?");
        if (dni.length() != 9 && isNumeric == false && dniChars.charAt(valNumDni) != ltrDNI) {
            return esValido = false;
        } else {
            return esValido;
        }
    }
}
