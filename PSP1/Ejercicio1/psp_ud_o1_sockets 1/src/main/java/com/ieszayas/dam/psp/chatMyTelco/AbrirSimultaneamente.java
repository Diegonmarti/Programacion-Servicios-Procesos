package com.ieszayas.dam.psp.chatMyTelco;

import java.io.File;
import java.io.IOException;

public class AbrirSimultaneamente {
/**
 * Clase que abre las dos interfaces
 * tanto el usuario como el cliente a la vez.
 * @version 1.00
 */
    public static void main(String[] arg) {
        String[] EditorTexto = {"java", "-cp", "target/classes", "--show-module-resolution", "com.ieszayas.dam.psp.chatMyTelco.Servidor"};
        String[] EditorTexto2 = {"java", "-cp", "target/classes", "--show-module-resolution", "com.ieszayas.dam.psp.chatMyTelco.Cliente"};
        try {
            ProcessBuilder proceso = new ProcessBuilder(EditorTexto);
            ProcessBuilder proceso2 = new ProcessBuilder(EditorTexto2);
            proceso.start();
            proceso2.start();
        } catch (IOException ex) {
            System.out.println("Error al abrir" + ex);
        }
    }
}
