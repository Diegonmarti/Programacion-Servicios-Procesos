package com.mycompany.proyecto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    public static void main(String[] args) {

        try {
            ServerSocket servidor = new ServerSocket(1500);
            System.out.println("Servidor iniciado: ");
            while (true) {
                Socket sc = servidor.accept();
                ServidorHilo s = new ServidorHilo(sc);
                s.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
