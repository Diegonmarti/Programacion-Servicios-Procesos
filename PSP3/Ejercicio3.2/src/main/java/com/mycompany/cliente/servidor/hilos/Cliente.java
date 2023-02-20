package com.mycompany.cliente.servidor.hilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    public static void main(String[] args) {
        try {
            /**
             * Inicializamos el puerto del cliente también por el puerto 1500
             * para que se conecte con el servidor.
             */
            Socket sc = new Socket("localhost", 1500);
            /**
             * Instanciamos el DataInputStream y el DataOutputStream para leer /
             * escribir datos primitivos.
             */
            DataInputStream entrada = new DataInputStream(sc.getInputStream());
            DataOutputStream salida = new DataOutputStream(sc.getOutputStream());

            Scanner sn = new Scanner(System.in);
            sn.useDelimiter("\n");

            System.out.println("Introduce la ruta del archivo a mostrar");
            String ruta = sn.next();

            salida.writeUTF(ruta);

            boolean existe = entrada.readBoolean();
            /**
             * Cuando reciba el boolean true de que existe la ruta, recorrera el
             * fichero, mostrando todo el contenido.
             */
            if (existe) {

                int longitud = entrada.readInt();

                byte[] contenido = new byte[longitud];

                for (int i = 0; i < longitud; i++) {
                    contenido[i] = entrada.readByte();
                }

                String contenidoFichero = new String(contenido);

                System.out.println(contenidoFichero);

            } else {
                System.out.println("Error, no existe el fichero");
            }

            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
