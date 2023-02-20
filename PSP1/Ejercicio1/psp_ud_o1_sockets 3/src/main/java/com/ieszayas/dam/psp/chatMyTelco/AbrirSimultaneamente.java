package com.ieszayas.dam.psp.chatMyTelco;

public class AbrirSimultaneamente {

    public static void main(String[] args) {
         String[] infoProceso = {"java", "-cp","target/classes","com.ieszayas.dam.psp.chatMyTelco.Servidor"};
          String[] infoProceso1 = {"java", "-cp","target/classes","com.ieszayas.dam.psp.chatMyTelco.Cliente"};
        try {
           
        //    String[] infoProceso1 = {"java", "-classpath C:\\Users\\Ntalia\\Dowlands\\psp_ud_01_sockets\\psp_ud_01_sockets\\psp_ud_01\\target\\classes com.ieszayas.dam.psp.chatMyTelco.Cliente"};
            Process proceso = new ProcessBuilder(infoProceso).start();
            Process proceso2 = new ProcessBuilder(infoProceso1).start();
        
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
