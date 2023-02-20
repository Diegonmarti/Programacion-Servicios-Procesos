package psp_07;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class PSP_07 {

    public static void main(String[] args) throws IOException {
        System.out.println("<=======================================>");
        System.out.println("PROGRAMA PARA ENCRIPTAR ARCHIVOS CON AES");
        System.out.println("<=======================================>");

        String nombreUsuario;
        String contraseña;
        String usuarioConcatenado;

        Scanner teclado = new Scanner(System.in);
        System.out.println("INDIQUE SU USUARIO: ");
        nombreUsuario = teclado.nextLine();
        System.out.println("INDIQUE SU CONTRASEÑA: ");
        contraseña = teclado.nextLine();

        if (nombreUsuario.equals("diego") && (contraseña.equals("1234"))) {
            usuarioConcatenado = nombreUsuario + contraseña;
            System.out.println(usuarioConcatenado);

            SecretKey clave = null;

            try {
                System.out.println("Escriba el contenido que desea incluir en el archivo: ");
                String contenido = teclado.nextLine();

                try {
                    File archivo = new File("fichero.txt");
                    FileWriter escritor = new FileWriter(archivo);
                    escritor.write(contenido);
                    escritor.close();
                    System.out.println("El archivo ha sido creado y escrito correctamente.");
                } catch (IOException e) {
                    System.out.println("Hubo un error al escribir en el archivo.");
                }
                clave = cifrarArchivo("fichero.txt", usuarioConcatenado);
                descifrarArchivo("fichero.txt.cifrado", clave, "fichero.txt.descifrado");
            } catch (Exception e) {
                e.printStackTrace();
            }
            muestraContenido("fichero.txt.descifrado");
        } else {
            System.out.println("No tienes acceso (El usuario es diego y contraseña: 1234)");
        }
    }

    private static SecretKey cifrarArchivo(String archivo, String contraseña) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        FileInputStream entrada = null; //archivo de entrada
        FileOutputStream salida = null; //archivo de salida
        int bytesLeidos;

        //1. Crear e inicializar clave
        System.out.println("1.- Genera clave Rijndael o AES");
        //crea un objeto para generar la clave usando algoritmo Rijndael o AES
        KeyGenerator generadorClave = KeyGenerator.getInstance("AES");
        SecureRandom numeroAleatorioSeguro = SecureRandom.getInstance("SHA1PRNG");
        numeroAleatorioSeguro.setSeed(contraseña.getBytes());
        //Longitud o tamaño de 128 Bits
        generadorClave.init(128, numeroAleatorioSeguro);
        SecretKey clave = generadorClave.generateKey(); //genera la clave privada

        //Puede comentarse, pues no es necesario para la ejecución pero es útil:
        System.out.println("Clave");
        mostrarBytes(clave.getEncoded()); //muestra la clave
        System.out.println();

        //Se Crea el objeto Cipher para cifrar, utilizando el algoritmo AES
        Cipher cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //Se inicializa el cifrador en modo CIFRADO o ENCRIPTACIÓN
        cifrador.init(Cipher.ENCRYPT_MODE, clave);
        System.out.println("2.- Cifrar con AES el archivo: " + archivo
                + ", y dejar resultado en " + archivo + ".cifrado");
        //declaración  de objetos
        byte[] buffer = new byte[1000];
        byte[] bufferCifrado;

        try {
            File archivo2 = new File("C:\\Users\\Administrador\\OneDrive\\Escritorio\\Grado Superior\\2ºDAM\\Programación de Servicios y Procesos\\Tema 7\\PSP_07\\fichero.txt");
            entrada = new FileInputStream(archivo2);
            salida = new FileOutputStream(archivo2 + ".cifrado");

            bytesLeidos = entrada.read(buffer, 0, 1000);
            while (bytesLeidos != -1) {
                bufferCifrado = cifrador.update(buffer, 0, bytesLeidos);
                salida.write(bufferCifrado);
                bytesLeidos = entrada.read(buffer, 0, 1000);
            }

            //Finaliza el cifrado
            bufferCifrado = cifrador.doFinal();
            salida.write(bufferCifrado);
        } catch (FileNotFoundException e) {
            System.err.println("No se ha encontrado el archivo porque está con la ruta de mi ordenador, tienes que cambiarla.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida");
        } catch (Exception e) {
            System.err.println("Error general");
        } finally {
            try {
                if (entrada != null) {
                    entrada.close();
                }
                if (salida != null) {
                    salida.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar los flujos");
            }
        }
        return clave;
    }

    private static void descifrarArchivo(String file, SecretKey clave, String fileDescifrado) {
        FileInputStream entrada = null;
        FileOutputStream salida = null;
        int bytesLeidos;
        try {
            System.out.println("3.- Descifrar con AES el archivo: " + file + ", y dejar resultado en " + fileDescifrado);
            Cipher cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cifrador.init(Cipher.DECRYPT_MODE, clave);
            byte[] buffer = new byte[1000];
            byte[] bufferDescifrado;
            entrada = new FileInputStream(file);
            salida = new FileOutputStream(fileDescifrado);
            bytesLeidos = entrada.read(buffer, 0, 1000);
            while (bytesLeidos != -1) {
                bufferDescifrado = cifrador.update(buffer, 0, bytesLeidos);
                salida.write(bufferDescifrado);
                bytesLeidos = entrada.read(buffer, 0, 1000);
            }
            bufferDescifrado = cifrador.doFinal();
            salida.write(bufferDescifrado);
        } catch (Exception e) {
            System.err.println("Error descifrando el archivo, este error suele dar porque no encuentra el archivo en la ruta.");
        } finally {
            try {
                if (entrada != null) {
                    entrada.close();
                }
                if (salida != null) {
                    salida.close();
                }
            } catch (Exception e) {
                System.err.println("Error al cerrar los ficheros: " + e);
            }
        }
    }

    private static void mostrarBytes(byte[] buf) {
        System.out.write(buf, 0, buf.length);
    }

    private static void muestraContenido(String fichero) {
        FileInputStream entrada = null;
        int bytesLeidos;
        System.out.println("4.- Mostrar contenido del archivo descifrado: " + fichero + ": ");
        try {
            entrada = new FileInputStream(fichero);
            byte[] buffer = new byte[1000];
            bytesLeidos = entrada.read(buffer, 0, 1000);
            while (bytesLeidos != -1) {
                System.out.write(buffer, 0, bytesLeidos);
                bytesLeidos = entrada.read(buffer, 0, 1000);
            }
            entrada.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error porque no se ha encontrado el archivo " + fichero);
        } catch (IOException e) {
            System.err.println("Error hubo un problema al leer el archivo " + fichero);
        } finally {
            System.out.println();
        }
    }

}
