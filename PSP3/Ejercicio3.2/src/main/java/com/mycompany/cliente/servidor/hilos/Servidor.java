package com.mycompany.cliente.servidor.hilos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    public static void main(String[] args) {
        try {
            /**
             * Creamos el cocket servidor con el puerto 1500 para que pueda
             * conectarse con el cliente.
             */
            ServerSocket servidor = new ServerSocket(1500);
            System.out.println("Servidor iniciado");
            /**
             * Cuando el Servidor reciba el boolean true del cliente (si se
             * conecta - true), entonces llamará a la clase hilos.
             */
            while (true) {

                Socket sc = servidor.accept();
                DataInputStream entrada = null;
                DataOutputStream salida = null;
                try {

                    entrada = new DataInputStream(sc.getInputStream());
                    salida = new DataOutputStream(sc.getOutputStream());

                    String ruta = entrada.readUTF();

                    File f = new File(ruta);

                    if (f.exists()) {
                        salida.writeBoolean(true);

                        BufferedReader leer = new BufferedReader(new FileReader(ruta));

                        String linea = "";
                        String contenido = "";

                        while ((linea = leer.readLine()) != null) {
                            contenido += linea + "\r\n";
                        }

                        leer.close();

                        byte[] contenidofile = contenido.getBytes();

                        salida.writeInt(contenidofile.length);

                        for (int i = 0; i < contenidofile.length; i++) {
                            salida.writeByte(contenidofile[i]);
                        }

                        sc.close();

                    } else {
                        salida.writeBoolean(false);
                    }
                } finally {
                    entrada.close();
                    salida.close();
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
