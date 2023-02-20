package com.ieszayas.dam.psp.chatMyTelco;

        import java.awt.Container;
        import java.awt.GridBagConstraints;
        import java.awt.GridBagLayout;
        import java.awt.Insets;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import javax.swing.*;

/**
 * Clase que abre un cuadro de diálogo para que
 * el usuario se identifique al abrir el CHAT
 * @version 1.00
 */
public class Acceso extends JDialog{

    private JTextField tfUsuario;
    private JTextField tfEmail;
    private JTextField tfHost;
    private JTextField tfPuerto;

    /**
     * Constructor de la ventana de configuracion inicial
     * Elementos:
     * Etiqueta y campo de captura para el email
     * Etiqueta y campo de captura para el nombre del usuario
     * Etiqueta y campo de captura para el nombre del host del socket (no visible)
     * Etiqueta y campo de captura para el numero de puerto del socket (no visible)
     * Boton Aceptar que cierra la ventana
     * @param padre Ventana padre (Panel del Chat)
     */

    public Acceso (JFrame padre, String puerto) {
        super(padre, "Identificación", true);

        JLabel lbEmail = new JLabel("Su email:");
        JLabel lbUsuario = new JLabel("Su nombre:");
        JLabel lbHost = new JLabel("Host:");lbHost.setVisible(false);
        JLabel lbPuerto = new JLabel("Puerto:");lbPuerto.setVisible(false);

        tfEmail = new JTextField();
        tfUsuario = new JTextField();
        tfHost = new JTextField("localhost");tfHost.setVisible(false);
        tfPuerto = new JTextField(puerto);tfPuerto.setVisible(false);

        JButton btAceptar = new JButton("Aceptar");
        btAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        Container c = this.getContentPane();
        c.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(20, 20, 0, 20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        c.add(lbEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        c.add(lbUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        c.add(lbHost, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        c.add(lbPuerto, gbc);

        gbc.ipadx = 100;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 1;
        gbc.gridy = 0;
        c.add(tfEmail, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        c.add(tfUsuario, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        c.add(tfHost, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        c.add(tfPuerto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        c.add(btAceptar, gbc);

        this.pack(); // Le da a la ventana el minimo tamaño posible
        this.setLocation(450, 200); // Posicion de la ventana
        this.setResizable(false); // Evita que se pueda estirar la ventana
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Deshabilita el boton de cierre de la ventana
        this.setVisible(true);
    }

    /**
     * Metodo publico que devuelve el email del usuario
     * @return
     */
    public String getEmail(){
        return this.tfUsuario.getText();
    }
    /**
     * Metodo publico que devuelve el nombre del usuario
     * @return
     */
    public String getUsuario(){
        return this.tfUsuario.getText();
    }

    /**
     * Metodo publico que devuelve el nombre de Host para el socket
     * @return
     */
    public String getHost(){
        return this.tfHost.getText();
    }

    /**
     * Metodo publico que devuelve el numero de puerto para el socket
     * @return
     */
    public int getPuerto(){
        return Integer.parseInt(this.tfPuerto.getText());
    }
}
