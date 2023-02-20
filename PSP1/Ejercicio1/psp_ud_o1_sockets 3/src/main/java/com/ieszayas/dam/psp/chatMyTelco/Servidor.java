package com.ieszayas.dam.psp.chatMyTelco;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase que hace de servidor del chat (Bot de atencion al cliente)
 * @version 1.00
 */
public class Servidor {
   
    private static int puerto = 1234;
    private static int maximoConexiones = 10; // Maximo de conexiones simultaneas
    private static ServerSocket servidor = null;
    private static Socket socket = null;
    private static int socketTimeout = 15000;
    private static InputStream is;
    private static OutputStream os;
    private static boolean activo = true;
    private static boolean inicio = true;
    private static String saludo = "MyTelco: Estimado cliente, en que podemos ayudarle...";
    private static String aviso = "MyTelco: Estimado cliente, en este momento no podemos ayudarle, intentelo o más tarde o envie un email...";
    /**
     * Metodo main
     * @param args
     */
    public static void main (String[] args) throws InterruptedException {
        puerto=Integer.parseInt( args[0]);
        try {
            // Se crea el serverSocket y se configura
            servidor = new ServerSocket(puerto, maximoConexiones);
            socket = servidor.accept();
            socket.setSoTimeout(socketTimeout);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            // Bucle infinito para esperar conexiones
            
            while (true) {
                try {
                    
                    if (activo) Responder();
                    else EnviarRespuesta(aviso);
                } catch (Exception ex) {
                    System.out.println("Servidor - Error: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println("Servidor - Error: " + ex.getMessage());
        } finally {
            try {
                socket.close();
                servidor.close();
            } catch (IOException ex) {
                System.out.println("Servidor - Error al cerrar el servidor: " + ex.getMessage());
            }
        }
    }
    /**
     * Metodo que devuelve la respuesta a la peticion de cliente
     */
    private static void Responder() throws IOException {
        if (inicio) {
            System.out.println("Servidor a la espera de conexiones.");
            System.out.println("Servidor - Cliente con la IP " + socket.getInetAddress().getHostName() + " conectado.");
            EnviarRespuesta(saludo);
            inicio = false;
        } else {
            try {
                BufferedReader peticionData = new BufferedReader(new InputStreamReader(is));
                String peticion = aviso;
                String linea;
                while ((linea = peticionData.readLine()) != null) {
                    if (linea.trim().length() > 0) {
                        if (peticion.equals(aviso)) peticion = "";
                        peticion += linea;
                    }
                    System.out.println("Servidor - peticion: " + peticion);
                    System.out.println("Servidor a la espera de una nueva comunicacion del socket.");
                    EnviarRespuesta(RespuestaPara(peticion));
                }
            } catch (Exception ex) {
                System.out.println("Servidor - Error al responder desde el servidor: " + ex.getMessage());
            }
        }
    }
    /**
     * Metodo que selecciona una respuesta entre las programadas
     * @param peticion (mensaje enviado por el cliente)
     * @return
     */
    private static String RespuestaPara (String peticion) {
        peticion = " " + peticion.toLowerCase().trim() + " ";
        if(peticion.contains("parar")){
            System.exit(0);
        }
        if (peticion.contains(" factura ") && peticion.contains(" incorrecta ")) {
            return "MyTelco: no se preocupe, informamos a nuestro departamento de facturacion";
        } else if (peticion.contains(" factura ") && peticion.contains(" elevada ")) {
            return "MyTelco: no se preocupe, informamos a nuestro departamento de facturacion";
        } else if (peticion.contains(" averia ") || peticion.contains(" error ")) {
            return "MyTelco: no se preocupe, informamos a nuestro departamento tecnico";
        } else if (peticion.contains(" adios ") || peticion.contains(" gracias ")) {
            return "MyTelco: gracias por contactar, estamos para ayudarle... ";
        } else {
            return "MyTelco: Estimado cliente, en que podemos ayudarle...";
        }
    }
    /**
     * Metodo que envia la respuesta al cliente a traves del socket
     * @param mensaje (respuesta al cliente)
     */
    private static void EnviarRespuesta (String mensaje) {
        try {
            DataOutputStream respuesta = new DataOutputStream(os);
            respuesta.writeUTF(mensaje);
            System.out.println("Servidor - respuesta desde el servidor: " + mensaje);
        } catch (Exception ex) {
            System.out.println("Servidor - Error al responder desde el servidor: " + ex.getMessage());
        }
    }
}

