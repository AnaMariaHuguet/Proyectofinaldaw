package com.proyectofinal.daw.components;

public class Utiles {

    public static boolean esDniValido(String dni) {
        String dniChars = "TRWAGMYFPDXBNJZSQVHLCKE";
        // boolean isNumeric;
        boolean esValido = true;

        String intPartDNI = dni.trim().replaceAll(" ", "").substring(0, 8);
        char ltrDNI = dni.charAt(8);

        int valNumDni = Integer.parseInt(intPartDNI) % 23;
        
        if (dniChars.charAt(valNumDni) != ltrDNI) {
            esValido = false;
        }
        return esValido;
    }
}
