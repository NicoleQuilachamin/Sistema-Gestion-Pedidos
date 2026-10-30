
package cafeteria.vista;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaPrincipal() {

        setTitle("Cafetería El Buen Sabor");
        setSize(600, 480);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 225, 235));
        add(panel);

        JLabel titulo = new JLabel("CAFETERÍA EL BUEN SABOR");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        titulo.setForeground(new Color(105, 60, 75));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBounds(45, 35, 495, 40);
        panel.add(titulo);

        JLabel subtitulo = new JLabel("Sistema de Gestión de Pedidos");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        subtitulo.setBounds(80, 80, 425, 25);
        panel.add(subtitulo);

        JButton btnProductos = new JButton("PRODUCTOS");
        btnProductos.setBounds(170, 135, 245, 48);
        panel.add(btnProductos);

        JButton btnPedidos = new JButton("PEDIDOS");
        btnPedidos.setBounds(170, 195, 245, 48);
        panel.add(btnPedidos);

        JButton btnReportes = new JButton("REPORTES");
        btnReportes.setBounds(170, 255, 245, 48);
        panel.add(btnReportes);

        JButton btnSalir = new JButton("SALIR");
        btnSalir.setBounds(170, 325, 245, 45);
        panel.add(btnSalir);

        Color rosa = new Color(245, 175, 198);

        btnProductos.setBackground(rosa);
        btnPedidos.setBackground(rosa);
        btnReportes.setBackground(rosa);
        btnSalir.setBackground(new Color(255, 248, 242));

        btnProductos.addActionListener(e -> {
            VentanaProductos ventana = new VentanaProductos();
            ventana.setVisible(true);
        });

        btnPedidos.addActionListener(e -> {
            VentanaPedidos ventana = new VentanaPedidos();
            ventana.setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            VentanaReportes ventana = new VentanaReportes();
            ventana.setVisible(true);
        });

        btnSalir.addActionListener(e -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
    }
}
