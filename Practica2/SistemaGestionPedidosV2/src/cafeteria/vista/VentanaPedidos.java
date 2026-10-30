
package cafeteria.vista;

import cafeteria.modelo.GestorCafeteria;
import cafeteria.modelo.Producto;
import cafeteria.modelo.Pedido;
import cafeteria.modelo.DetallePedido;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaPedidos extends JFrame {

    private static final long serialVersionUID = 1L;

    private GestorCafeteria gestor;
    private Pedido pedido;

    private JTextField txtCodigo;
    private JTextField txtCantidad;

    private DefaultTableModel modeloTabla;

    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;

    public VentanaPedidos() {

        gestor = new GestorCafeteria();
        pedido = new Pedido(gestor.obtenerSiguienteNumeroPedido());

        setTitle("Cafetería El Buen Sabor - Pedidos");
        setSize(750, 540);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 225, 235));
        add(panel);

        Color cafe = new Color(105, 60, 75);
        Color rosa = new Color(245, 175, 198);

        JLabel titulo = new JLabel("GESTIÓN DE PEDIDOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(cafe);
        titulo.setBounds(240, 25, 350, 40);
        panel.add(titulo);

        JLabel lblCodigo = new JLabel("CÓDIGO:");
        lblCodigo.setBounds(50, 120, 90, 25);
        panel.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(145, 120, 170, 28);
        panel.add(txtCodigo);

        JLabel lblCantidad = new JLabel("CANTIDAD:");
        lblCantidad.setBounds(50, 175, 90, 25);
        panel.add(lblCantidad);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(145, 175, 170, 28);
        panel.add(txtCantidad);

        JButton btnAgregar = new JButton("AGREGAR PRODUCTO");
        btnAgregar.setBounds(90, 245, 225, 40);
        btnAgregar.setBackground(rosa);
        panel.add(btnAgregar);

        JLabel lblDetalle = new JLabel("DETALLE DEL PEDIDO");
        lblDetalle.setBounds(445, 100, 200, 25);
        lblDetalle.setForeground(cafe);
        panel.add(lblDetalle);

        modeloTabla = new DefaultTableModel(
            new String[]{"Producto", "Cantidad", "Subtotal"}, 0
        ) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(365, 140, 325, 150);
        panel.add(scroll);

        lblSubtotal = new JLabel("SUBTOTAL: $0.00");
        lblSubtotal.setBounds(460, 305, 230, 25);
        panel.add(lblSubtotal);

        lblIva = new JLabel("IVA (15%): $0.00");
        lblIva.setBounds(460, 335, 230, 25);
        panel.add(lblIva);

        lblTotal = new JLabel("TOTAL A PAGAR: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(cafe);
        lblTotal.setBounds(460, 365, 255, 30);
        panel.add(lblTotal);

        JButton btnConfirmar = new JButton("CONFIRMAR PEDIDO");
        btnConfirmar.setBounds(380, 425, 210, 40);
        btnConfirmar.setBackground(rosa);
        panel.add(btnConfirmar);

        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(150, 425, 150, 40);
        panel.add(btnVolver);

        btnAgregar.addActionListener(e -> agregarProducto());

        btnConfirmar.addActionListener(e -> confirmarPedido());

        btnVolver.addActionListener(e -> dispose());
    }

    private void agregarProducto() {

        try {
            int codigo = Integer.parseInt(txtCodigo.getText());
            int cantidad = Integer.parseInt(txtCantidad.getText());

            if (codigo <= 0 || cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Ingresa valores mayores a cero.");
                return;
            }

            Producto producto = gestor.buscarProducto(codigo);

            if (producto == null) {
                JOptionPane.showMessageDialog(this,
                    "El producto no existe.");
                return;
            }

            int cantidadTotal = cantidad;

            for (DetallePedido detalle : pedido.getDetalles()) {
                if (detalle.getProducto().getCodigo() == codigo) {
                    cantidadTotal += detalle.getCantidad();
                }
            }

            if (cantidadTotal > producto.getStock()) {
                JOptionPane.showMessageDialog(this,
                    "Stock insuficiente.");
                return;
            }

            DetallePedido detalle = new DetallePedido(producto, cantidad);
            pedido.agregarDetalle(detalle);

            modeloTabla.addRow(new Object[]{
                producto.getNombre(),
                cantidad,
                String.format("%.2f", detalle.getSubtotal())
            });

            actualizarTotales();

            txtCodigo.setText("");
            txtCantidad.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Ingresa un código y una cantidad válidos.");
        }
    }

    private void confirmarPedido() {

        if (pedido.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Primero agrega productos.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            String.format(
                "Subtotal: $%.2f\nIVA: $%.2f\nTotal: $%.2f\n\n¿Confirmar pedido?",
                pedido.getSubtotal(),
                pedido.getIva(),
                pedido.getTotalConIva()
            ),
            "Confirmar pedido",
            JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            if (gestor.registrarPedido(pedido)) {

                JOptionPane.showMessageDialog(this,
                    "Pedido registrado correctamente.");

                pedido = new Pedido(gestor.obtenerSiguienteNumeroPedido());

                modeloTabla.setRowCount(0);
                actualizarTotales();

                txtCodigo.setText("");
                txtCantidad.setText("");

            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el pedido.");
            }
        }
    }

    private void actualizarTotales() {

        lblSubtotal.setText(
            String.format("SUBTOTAL: $%.2f", pedido.getSubtotal())
        );

        lblIva.setText(
            String.format("IVA (15%%): $%.2f", pedido.getIva())
        );

        lblTotal.setText(
            String.format("TOTAL A PAGAR: $%.2f", pedido.getTotalConIva())
        );
    }
}
