package com.mycompany.proyecto;

public class Aleatorios {
    public static void main(String[]args){
        int cantidadGenerados = 40;
        for (int i = 0; i < cantidadGenerados; i++) {
            System.out.println(generaNumeroAleatorio(0,100)+" ");
        }
    }
    
    public static int generaNumeroAleatorio(int minimo, int maximo){
        int num=(int)(Math.random()*(maximo-minimo)+(minimo));
        return num;
    }
}
