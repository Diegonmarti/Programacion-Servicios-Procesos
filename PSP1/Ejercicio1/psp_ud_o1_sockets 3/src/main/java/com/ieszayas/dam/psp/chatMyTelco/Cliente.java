package com.ieszayas.dam.psp.chatMyTelco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Clase que contiene el panel del chat Se utiliza el componente visual JFrame
 *
 * @version 1.00
 */
public class Cliente extends JFrame {
  
    private JTextArea mensajesChat;
    private Socket socket;
    private int puerto;
    private String host;
    private String usuario;
    private DataOutputStream salidaDatos;
    private DataInputStream entradaDatos;
    private final String bienvenida = "MyTelco: Bienvenido estimado cliente, escriba su mensaje...";
    private Acceso vc;
    private int socketTimeout = 15000;

    public static void main(String[] args) {
        Cliente c = new Cliente();
    }

    /**
     * Constructor de la clase
     */
    public Cliente() {
        super("MyTelco Chat");
        int valorinicial = 0;
        int minimo = 0;
        int maximo = 9999;
        do {
            valorinicial = (int) Math.floor(Math.random() * (minimo - maximo + 1) + maximo);
        } while (puertos(valorinicial) == false);
        puerto = valorinicial;
        try{
        Process proceso = new ProcessBuilder("java", "-cp","target/classes","com.ieszayas.dam.psp.chatMyTelco.Servidor", puerto+"").start();
        }catch(Exception e){
            System.out.println("Error");
        }     
        // Elementos de la ventana
        mensajesChat = new JTextArea();
        mensajesChat.setEnabled(false); // El area de mensajes del chat no se debe de poder editar
        mensajesChat.setLineWrap(true); // Las lineas se parten al llegar al ancho del textArea
        mensajesChat.setWrapStyleWord(true); // Las lineas se parten entre palabras (por los espacios blancos)
        JScrollPane scrollMensajesChat = new JScrollPane(mensajesChat);
        JTextField tfMensaje = new JTextField("");
        JButton btEnviar = new JButton("Enviar");
        JButton btCerrar = new JButton("Cerrar Chat");
        // Colocacion de los componentes en la ventana
        Container c = this.getContentPane();
        c.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        c.add(scrollMensajesChat, gbc);
        // Restaura valores por defecto
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        c.add(tfMensaje, gbc);
        // Restaura valores por defecto
        gbc.weightx = 0;
        gbc.gridx = 1;
        gbc.gridy = 1;
        c.add(btEnviar, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        c.add(btCerrar, gbc);
        this.setBounds(400, 100, 400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Deshabilita el boton de cierre de la ventana
        // Invocacion de la pantalla de acceso
        vc = new Acceso(this, puerto+"");
        // Apertura del socket
        socket = socketAbrir();
        // IMPORTANTE : establecemos un limite de espera para poder trabajar en multiproceso (.readline())
        if (socket != null) {
            try {
                socket.setSoTimeout(socketTimeout);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }
        //
        socketRecibir();
        // Evento del Boton enviar (usamos funcion anonima)
        btEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String mensaje = vc.getUsuario() + ": " + tfMensaje.getText();
                    mensajesChat.append(mensaje + System.lineSeparator());
                } catch (Exception ex) {
                    System.out.println("no se ha podido actualizar la pantalla (" + ex.getMessage() + ").");
                }
                try {
                    socketEnviar(tfMensaje.getText());
                    tfMensaje.setText("");
                    try {
                        socketRecibir();
                    } catch (Exception ex) {
                        System.out.println("no se ha podido recibir desde el socket (" + ex.getMessage() + ").");
                    }
                } catch (Exception ex) {
                    System.out.println("no se ha podido enviar al socket (" + ex.getMessage() + ").");
                }
            }
        });
        // Evento del Boton cerrar (usamos funcion anonima)
        btCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    socketEnviar("parar");
                    socket.close();
                    System.exit(0);
                } catch (Exception ex) {
                    System.out.println("no se ha cerrar la pantalla (" + ex.getMessage() + ").");
                }
            }
        });

    }

    /**
     * método que abre el socket que comunica con el servidor del chat
     *
     * @return socket abierto
     */
    private Socket socketAbrir() {
        try {
            System.out.println("Quieres conectarte a " + vc.getHost() + " en el puerto " + vc.getPuerto() + " con el email: " + vc.getUsuario() + ".");
            return new Socket(vc.getHost(), vc.getPuerto());

        } catch (UnknownHostException ex) {
            System.out.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
            return null;

        } catch (IOException ex) {
            System.out.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");

            System.out.println("El socket no se creo... ");
            return null;
        }
    }

    /**
     * Metodo que envía al servidor del chat un mensaje a traves del socket
     * abierto
     *
     * @param mensaje
     */
    private void socketEnviar(String mensaje) {
        try {
            this.salidaDatos = new DataOutputStream(socket.getOutputStream());
            this.salidaDatos.writeUTF(mensaje + System.lineSeparator());
        } catch (IOException ex) {
            System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("El socket no se creo correctamente. ");
        }
        // Enviamos el mensaje

    }

    /**
     * Recibe los mensajes del chat reenviados por el servidor
     */
    private void socketRecibir() {
        // Obtiene el flujo de entrada del socket
        String mensaje;
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("El socket no se creo correctamente. ");
        }
        try {
            mensaje = entradaDatos.readUTF();
            if (mensaje.length() > 0) {
                mensajesChat.append(mensaje + System.lineSeparator());
            }
        } catch (IOException ex) {
            System.out.println("Error al leer del stream de entrada: " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("El socket no se creo correctamente. ");
        }
    }
    public static boolean puertos(int port) {

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
}
