
package cafeteria.vista;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import cafeteria.modelo.GestorCafeteria;
import cafeteria.modelo.Producto;

public class VentanaProductos extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;

    private JTable tabla;
    private DefaultTableModel modelo;
    private GestorCafeteria gestor;

    public VentanaProductos() {

        gestor = new GestorCafeteria();

        setTitle("Cafetería El Buen Sabor - Productos");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 225, 235));
        add(panel);

        Color cafe = new Color(105, 60, 75);
        Color rosa = new Color(245, 175, 198);

        JLabel titulo = new JLabel("GESTIÓN DE PRODUCTOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(cafe);
        titulo.setBounds(210, 25, 400, 40);
        panel.add(titulo);

        JLabel lblCodigo = new JLabel("CÓDIGO:");
        lblCodigo.setBounds(40, 105, 100, 25);
        panel.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(140, 105, 175, 28);
        panel.add(txtCodigo);

        JLabel lblNombre = new JLabel("NOMBRE:");
        lblNombre.setBounds(40, 150, 100, 25);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(140, 150, 175, 28);
        panel.add(txtNombre);

        JLabel lblPrecio = new JLabel("PRECIO:");
        lblPrecio.setBounds(40, 195, 100, 25);
        panel.add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(140, 195, 175, 28);
        panel.add(txtPrecio);

        JLabel lblStock = new JLabel("STOCK:");
        lblStock.setBounds(40, 240, 100, 25);
        panel.add(lblStock);

        txtStock = new JTextField();
        txtStock.setBounds(140, 240, 175, 28);
        panel.add(txtStock);

        JLabel lblTabla = new JLabel("PRODUCTOS REGISTRADOS");
        lblTabla.setBounds(420, 95, 250, 25);
        lblTabla.setForeground(cafe);
        panel.add(lblTabla);

        modelo = new DefaultTableModel(
            new String[]{"Código", "Nombre", "Precio", "Stock"}, 0
        ) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setBackground(rosa);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(365, 130, 325, 170);
        panel.add(scroll);

        JButton btnRegistrar = new JButton("REGISTRAR");
        btnRegistrar.setBounds(95, 335, 150, 40);
        btnRegistrar.setBackground(rosa);
        panel.add(btnRegistrar);

        JButton btnEditar = new JButton("EDITAR");
        btnEditar.setBounds(255, 335, 150, 40);
        btnEditar.setBackground(rosa);
        panel.add(btnEditar);

        JButton btnStock = new JButton("REPONER STOCK");
        btnStock.setBounds(415, 335, 180, 40);
        btnStock.setBackground(rosa);
        panel.add(btnStock);

        JButton btnListar = new JButton("LISTAR");
        btnListar.setBounds(205, 390, 150, 38);
        panel.add(btnListar);

        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(375, 390, 150, 38);
        panel.add(btnVolver);

        btnRegistrar.addActionListener(e -> registrarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnStock.addActionListener(e -> reponerStock());
        btnListar.addActionListener(e -> actualizarTabla());
        btnVolver.addActionListener(e -> dispose());

        tabla.getSelectionModel().addListSelectionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {
                txtCodigo.setText(tabla.getValueAt(fila, 0).toString());
                txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                txtPrecio.setText(tabla.getValueAt(fila, 2).toString());
                txtStock.setText(tabla.getValueAt(fila, 3).toString());
            }
        });

        actualizarTabla();
    }

    private void registrarProducto() {

        try {
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            if (codigo <= 0 || nombre.isEmpty()
                    || !Double.isFinite(precio)
                    || precio <= 0 || stock < 0) {

                JOptionPane.showMessageDialog(this,
                    "Revisa los datos del producto.");
                return;
            }

            if (gestor.buscarProducto(codigo) != null) {
                JOptionPane.showMessageDialog(this,
                    "El código ya existe.");
                return;
            }

            Producto producto = new Producto(codigo, nombre, precio, stock);
            gestor.registrarProducto(producto);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                "Producto registrado correctamente.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Ingresa números válidos.");
        }
    }

    private void editarProducto() {

        try {
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            if (!Double.isFinite(precio)) {
                JOptionPane.showMessageDialog(this,
                    "El precio no es válido.");
                return;
            }

            boolean editado = gestor.editarProducto(
                codigo, nombre, precio, stock
            );

            if (editado) {
                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(this,
                    "Producto actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo editar el producto.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Ingresa números válidos.");
        }
    }

    private void reponerStock() {

        try {
            int codigo = Integer.parseInt(txtCodigo.getText().trim());

            String respuesta = JOptionPane.showInputDialog(
                this, "¿Cuántas unidades deseas agregar?"
            );

            if (respuesta == null) {
                return;
            }

            int cantidad = Integer.parseInt(respuesta.trim());

            if (gestor.aumentarStock(codigo, cantidad)) {
                actualizarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(this,
                    "Stock actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Producto no encontrado o cantidad incorrecta.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Ingresa un código y una cantidad válidos.");
        }
    }

    private void actualizarTabla() {

        modelo.setRowCount(0);

        for (Producto producto : gestor.obtenerProductos()) {

            modelo.addRow(new Object[]{
                producto.getCodigo(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock()
            });
        }
    }

    private void limpiarCampos() {

        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        tabla.clearSelection();
    }
}
