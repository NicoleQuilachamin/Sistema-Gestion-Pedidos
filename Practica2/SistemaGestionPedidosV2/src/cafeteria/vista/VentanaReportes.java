
package cafeteria.vista;

import cafeteria.modelo.GestorCafeteria;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class VentanaReportes extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel panel;
    private GestorCafeteria gestor;

    public VentanaReportes() {

        gestor = new GestorCafeteria();

        setTitle("Cafetería El Buen Sabor - Reportes");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 225, 235));
        add(panel);

        mostrarMenu();
    }

    private void mostrarMenu() {

        panel.removeAll();

        JLabel titulo = new JLabel("REPORTES DE VENTAS");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(105, 60, 75));
        titulo.setBounds(240, 35, 350, 45);
        panel.add(titulo);

        JLabel mensaje = new JLabel("SELECCIONA EL REPORTE QUE DESEAS VER");
        mensaje.setBounds(220, 95, 350, 30);
        panel.add(mensaje);

        JButton btnHoy = new JButton("VENTAS DE HOY");
        btnHoy.setBounds(245, 150, 240, 45);
        btnHoy.setBackground(new Color(245, 175, 198));
        panel.add(btnHoy);

        JButton btnHistorial = new JButton("HISTORIAL GENERAL");
        btnHistorial.setBounds(245, 215, 240, 45);
        btnHistorial.setBackground(new Color(245, 175, 198));
        panel.add(btnHistorial);

        JButton btnProductos = new JButton("PRODUCTOS MÁS VENDIDOS");
        btnProductos.setBounds(245, 280, 240, 45);
        btnProductos.setBackground(new Color(245, 175, 198));
        panel.add(btnProductos);

        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(290, 375, 150, 40);
        panel.add(btnVolver);

        btnHoy.addActionListener(e -> mostrarReporte(1));
        btnHistorial.addActionListener(e -> mostrarReporte(2));
        btnProductos.addActionListener(e -> mostrarReporte(3));
        btnVolver.addActionListener(e -> dispose());

        panel.revalidate();
        panel.repaint();
    }

    private void mostrarReporte(int tipo) {

        panel.removeAll();

        gestor = new GestorCafeteria();

        String titulo = "";

        if (tipo == 1) {
            titulo = "VENTAS DE HOY";
        } else if (tipo == 2) {
            titulo = "HISTORIAL GENERAL";
        } else {
            titulo = "PRODUCTOS MÁS VENDIDOS";
        }

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(105, 60, 75));
        lblTitulo.setBounds(180, 35, 450, 45);
        panel.add(lblTitulo);

        JTextArea txtReporte = new JTextArea();
        txtReporte.setEditable(false);
        txtReporte.setFont(new Font("Arial", Font.PLAIN, 14));
        txtReporte.setLineWrap(true);
        txtReporte.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtReporte);
        scroll.setBounds(95, 120, 545, 240);
        panel.add(scroll);

        String reporte = gestor.obtenerReporte();
        String resultado = "";

        if (tipo == 1) {

            int inicio = reporte.indexOf("--- VENTAS DE HOY");
            int fin = reporte.indexOf("--- HISTORIAL GENERAL");

            if (inicio >= 0 && fin >= 0) {
                resultado = reporte.substring(inicio, fin);
            }

        } else if (tipo == 2) {

            int inicio = reporte.indexOf("--- HISTORIAL GENERAL");
            int fin = reporte.indexOf("--- PRODUCTOS MAS VENDIDOS");

            if (inicio >= 0 && fin >= 0) {
                resultado = reporte.substring(inicio, fin);
            }

        } else {

            int inicio = reporte.indexOf("--- PRODUCTOS MAS VENDIDOS");

            if (inicio >= 0) {
                resultado = reporte.substring(inicio);
            }
        }

        if (resultado.isEmpty()) {
            resultado = "No hay información disponible.";
        }

        txtReporte.setText(resultado);

        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(290, 385, 150, 40);
        panel.add(btnVolver);

        btnVolver.addActionListener(e -> mostrarMenu());

        panel.revalidate();
        panel.repaint();
    }
}
