package com.mycompany.proyecto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Actividad6b {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicita el nombre del usuario
        System.out.print("Ingrese su nombre de usuario (8 caracteres, solo letras minúsculas): ");
        String username = scanner.nextLine();

        // Valida el nombre del usuario
        while (!validateUsername(username)) {
            System.out.println("Nombre de usuario no válido. Por favor, intente de nuevo.");
            System.out.print("Ingrese su nombre de usuario (8 caracteres, solo letras minúsculas): ");
            username = scanner.nextLine();
        }

        // Solicita el nombre del archivo
        System.out.print("Ingrese el nombre del archivo (8 caracteres, con extensión de 3 caracteres): ");
        String filename = scanner.nextLine();

        // Valida el nombre del archivo
        while (!validateFilename(filename)) {
            System.out.println("Nombre de archivo no válido. Por favor, intente de nuevo.");
            System.out.print("Ingrese el nombre del archivo (8 caracteres, con extensión de 3 caracteres): ");
            filename = scanner.nextLine();
        }

        // Obtiene el directorio actual
        File currentDir = new File(".");
        String currentDirPath = currentDir.getAbsolutePath();
        
        // Si el directorio actual no es "c:\datos", rechaza el acceso
        if (!currentDirPath.equals("c:\\datos")) {
            System.out.println("Acceso denegado: no está autorizado para abrir archivos en este directorio.");
            return;
        }
        
        // Muestra el contenido del archivo
        try {
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo " + filename);
            e.printStackTrace();
        }

        // Lleva un registro de la actividad del programa
        logActivity(username, filename);
    }

    public static boolean validateUsername(String username) {
        // Valida que el nombre de usuario tenga 8 caracteres y esté compuesto únicamente por letras minúsculas
        return username.matches("^[a-z]{8}$");
    }

    public static boolean validateFilename(String filename) {
        // Valida que el nombre del archivo tenga 8 caracteres y una extensión de 3 caracteres
        return filename.matches("^.{8}\\..{3}$");
    }

    public static void logActivity(String username, String filename) {
        // Lleva un registro de la actividad del programa (ejemplo: escribe en un archivo de texto)
        try {
            FileWriter fw = new FileWriter("activity_log.txt", true);
            fw.write(username + " ha abierto el archivo " + filename + " en fecha " + new Date() + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de registro de actividad");
            e.printStackTrace();
        }
    }
}
