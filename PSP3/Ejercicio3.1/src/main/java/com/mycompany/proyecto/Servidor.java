package com.mycompany.proyecto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    public static void main(String[] args) {

        try {
            ServerSocket servidor = new ServerSocket(2000);
            System.out.println("Servidor iniciado: ");
            while (true) {
                Socket socket = servidor.accept();
                DataInputStream entrada = null;
                DataOutputStream salida = null;
                System.out.println("Cliente conectado");
                try {
                    entrada = new DataInputStream(socket.getInputStream());
                    salida = new DataOutputStream(socket.getOutputStream());
                    int numeroAleatorio = Aleatorios.generaNumeroAleatorio(1, 100);
                    int numeroUsuario = 0;

                    System.out.println("Num generado: " + numeroAleatorio);
                    do {
                        salida.writeUTF("Escribe un numero entre el 1 y el 100");

                        numeroUsuario = entrada.readInt();
                        System.out.println("Numero recibido: " + numeroUsuario);
                        if (numeroUsuario == numeroAleatorio) {
                            salida.writeUTF("Has ganado");
                        } else if (numeroUsuario < numeroAleatorio) {
                            salida.writeUTF("El numero buscado es mayor");
                        } else {
                            salida.writeUTF("El numero buscado es menor");
                        }

                        salida.writeBoolean(numeroUsuario == numeroAleatorio);
                    } while (numeroUsuario != numeroAleatorio);
                    socket.close();
                    System.out.println("Cliente desconectado");
                } catch (IOException ex) {
                    System.out.println("Error");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
