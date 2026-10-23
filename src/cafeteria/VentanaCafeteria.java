package cafeteria;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VentanaCafeteria extends JFrame {

    private GestorCafeteria gestor = new GestorCafeteria();

    Color rosa = new Color(255, 190, 200);
    Color rosaClaro = new Color(255, 225, 230);
    Color crema = new Color(255, 245, 235);
    Color cafe = new Color(105, 60, 45);
    Color cafeClaro = new Color(225, 190, 165);

    public VentanaCafeteria() {

        setTitle("Cafetería El Buen Sabor");
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(crema);

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(rosa);
        encabezado.setBorder(new EmptyBorder(25, 10, 25, 10));

        JLabel titulo = new JLabel("CAFETERÍA");
        titulo.setFont(new Font("Serif", Font.BOLD, 38));
        titulo.setForeground(cafe);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("EL BUEN SABOR");
        subtitulo.setFont(new Font("Serif", Font.BOLD, 23));
        subtitulo.setForeground(cafe);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel frase = new JLabel("Buen café, mejores días");
        frase.setFont(new Font("Serif", Font.ITALIC, 17));
        frase.setForeground(cafe);
        frase.setAlignmentX(Component.CENTER_ALIGNMENT);

        encabezado.add(titulo);
        encabezado.add(subtitulo);
        encabezado.add(Box.createVerticalStrut(10));
        encabezado.add(frase);

        JPanel menu = new JPanel(new GridLayout(6, 1, 0, 15));
        menu.setBackground(crema);
        menu.setBorder(new EmptyBorder(30, 75, 30, 75));

        JButton btnRegistrar = crearBoton("Registrar producto", rosaClaro);

        JButton btnListar = crearBoton("Listar productos", cafeClaro);
        btnListar.addActionListener(e -> listarProductos());

        JButton btnStock = crearBoton("Reponer stock", rosaClaro);
        btnStock.addActionListener(e -> reponerStock());

        JButton btnPedido = crearBoton("Registrar pedido", cafeClaro);
        btnPedido.addActionListener(e -> registrarPedido());

        JButton btnVentas = crearBoton("Reporte de ventas", rosaClaro);
        btnVentas.addActionListener(e -> mostrarReporte());

        JButton btnSalir = crearBoton("Salir", cafe);
        btnSalir.setForeground(Color.WHITE);

        menu.add(btnRegistrar);
        menu.add(btnListar);
        menu.add(btnStock);
        menu.add(btnPedido);
        menu.add(btnVentas);
        menu.add(btnSalir);

        btnRegistrar.addActionListener(e -> registrarProducto());

        btnSalir.addActionListener(e -> {

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas salir de la cafetería?",
                    "Salir",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        principal.add(encabezado, BorderLayout.NORTH);
        principal.add(menu, BorderLayout.CENTER);

        add(principal);
    }

    public JButton crearBoton(String texto, Color color) {

        JButton boton = new JButton(texto);

        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setBackground(color);
        boton.setForeground(cafe);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(cafe, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));

        return boton;
    }

    private void registrarProducto() {

        JTextField txtCodigo = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(crema);

        panel.add(new JLabel("Código:"));
        panel.add(txtCodigo);

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);

        panel.add(new JLabel("Stock:"));
        panel.add(txtStock);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Registrar producto",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {

            try {

                int codigo = Integer.parseInt(txtCodigo.getText().trim());
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());

                if (codigo <= 0 || nombre.isEmpty()
                        || nombre.contains(";")
                        || precio <= 0 || stock < 0
                        || !Double.isFinite(precio)) {

                    JOptionPane.showMessageDialog(
                            this,"Revisa los datos ingresados.");
                    return;
                }

                if (gestor.buscarProducto(codigo) != null) {
                	JOptionPane.showMessageDialog(this,"Ya existe un producto con ese código."
                    );
                    return;
                }
                Producto producto = new Producto(codigo, nombre, precio, stock
                );
                gestor.registrarProducto(producto);
                JOptionPane.showMessageDialog(
                        this,"Producto registrado correctamente."
                );
            } catch (NumberFormatException ex) {
            	JOptionPane.showMessageDialog(
                        this,"Ingresa números válidos en código, precio y stock."
                );
            }
        }
    }

    private void listarProductos() {

        java.util.ArrayList<Producto> productos = gestor.obtenerProductos();

        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay productos registrados."
            );
            return;
        }

        String[] columnas = {"Código", "Nombre", "Precio", "Stock"};
        Object[][] datos = new Object[productos.size()][4];

        for (int i = 0; i < productos.size(); i++) {

            Producto p = productos.get(i);

            datos[i][0] = p.getCodigo();
            datos[i][1] = p.getNombre();
            datos[i][2] = String.format("%.2f", p.getPrecio());
            datos[i][3] = p.getStock();
        }

        JTable tabla = new JTable(datos, columnas);
        tabla.setEnabled(false);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabla.getTableHeader().setBackground(rosa);
        tabla.getTableHeader().setForeground(cafe);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(500, 250));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Productos registrados",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void reponerStock() {

        try {

            String datoCodigo = JOptionPane.showInputDialog(
                    this, "Ingrese el código del producto:"
            );

            if (datoCodigo == null) {
                return;
            }

            int codigo = Integer.parseInt(datoCodigo.trim());

            Producto producto = gestor.buscarProducto(codigo);

            if (producto == null) {
                JOptionPane.showMessageDialog(
                        this, "Producto no encontrado."
                );
                return;
            }

            String datoCantidad = JOptionPane.showInputDialog(
                    this, "Cantidad que desea agregar:"
            );

            if (datoCantidad == null) {
                return;
            }

            int cantidad = Integer.parseInt(datoCantidad.trim());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(
                        this, "La cantidad debe ser mayor que cero."
                );
                return;
            }

            if (cantidad > Integer.MAX_VALUE - producto.getStock()) {
                JOptionPane.showMessageDialog(
                        this, "La cantidad es demasiado grande."
                );
                return;
            }

            gestor.reponerStock(codigo, cantidad);

            JOptionPane.showMessageDialog(
                    this, "Stock actualizado correctamente."
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Ingrese solamente números enteros."
            );
        }
    }

    private void registrarPedido() {

        int numero = gestor.obtenerSiguienteNumeroPedido();
        Pedido pedido = new Pedido(numero);

        while (true) {

            String datoCodigo = JOptionPane.showInputDialog(
                    this,
                    "Código del producto (0 para terminar):"
            );

            if (datoCodigo == null) {
                return;
            }

            int codigo;

            try {
                codigo = Integer.parseInt(datoCodigo.trim());

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this, "Ingresa un código válido."
                );
                continue;
            }

            if (codigo == 0) {
                break;
            }

            Producto producto = gestor.buscarProducto(codigo);

            if (producto == null) {
                JOptionPane.showMessageDialog(
                        this, "Producto no encontrado."
                );
                continue;
            }

            String datoCantidad = JOptionPane.showInputDialog(
                    this,
                    producto.getNombre() + "\nCantidad:"
            );

            if (datoCantidad == null) {
                return;
            }

            int cantidad;

            try {
                cantidad = Integer.parseInt(datoCantidad.trim());

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this, "Ingresa una cantidad válida."
                );
                continue;
            }

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(
                        this, "La cantidad debe ser mayor que cero."
                );
                continue;
            }

            int cantidadAcumulada = cantidad;

            for (DetallePedido detalle : pedido.getDetalles()) {

                if (detalle.getProducto().getCodigo() == codigo) {
                    cantidadAcumulada += detalle.getCantidad();
                }
            }

            if (cantidadAcumulada > producto.getStock()) {
                JOptionPane.showMessageDialog(
                        this, "Stock insuficiente."
                );
                continue;
            }

            DetallePedido detalle =
                    new DetallePedido(producto, cantidad);

            pedido.agregarDetalle(detalle);

            JOptionPane.showMessageDialog(
                    this,
                    "Producto agregado: " + producto.getNombre()
                    + "\nSubtotal: $"
                    + String.format("%.2f", detalle.getSubtotal())
            );
        }

        if (pedido.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "No se agregaron productos."
            );
            return;
        }

        gestor.registrarPedido(pedido);

        JOptionPane.showMessageDialog(
                this,
                "Pedido N.º " + numero
                + "\nTotal: $" + String.format("%.2f", pedido.getTotal())
        );
    }

    private void mostrarReporte() {

        String reporte = gestor.obtenerReporte();

        JTextArea area = new JTextArea(reporte);
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        area.setBackground(crema);
        area.setForeground(cafe);
        area.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 250));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Reporte de ventas",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaCafeteria ventana = new VentanaCafeteria();
            ventana.setVisible(true);

        });
    }
}
