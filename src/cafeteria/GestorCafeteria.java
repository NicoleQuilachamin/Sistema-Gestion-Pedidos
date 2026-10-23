package cafeteria;

import java.util.ArrayList;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GestorCafeteria {

    private ArrayList<Producto> productos;
    private ArrayList<Pedido> pedidos;

    public GestorCafeteria() {
        productos = new ArrayList<>();
        pedidos = new ArrayList<>();
        cargarProductos();
        cargarPedidos();
    }

    public void registrarProducto(Producto producto) {

        for (Producto p : productos) {
            if (p.getCodigo() == producto.getCodigo()) {
                System.out.println("Error: el codigo ya existe.");
                return;
            }
        }

        if (producto.getPrecio() <= 0 || producto.getStock() < 0) {
            System.out.println("Error: precio o stock no valido.");
            return;
        }

        productos.add(producto);
        guardarProductos();

        System.out.println("Producto registrado correctamente.");
    }

    public void listarProductos() {

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.println("\n--- PRODUCTOS DISPONIBLES ---");

        for (Producto p : productos) {
            System.out.println(
                "Codigo: " + p.getCodigo()
                + " | Nombre: " + p.getNombre()
                + " | Precio: $" + p.getPrecio()
                + " | Stock: " + p.getStock()
            );
        }
    }

    public Producto buscarProducto(int codigo) {

        for (Producto p : productos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }

        return null;
    }

    public void reponerStock(int codigo, int cantidad) {

        Producto producto = buscarProducto(codigo);

        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor que cero.");
            return;
        }

        producto.setStock(producto.getStock() + cantidad);
        guardarProductos();

        System.out.println("Stock actualizado correctamente.");
    }

    public void registrarPedido(Pedido pedido) {

        if (pedido.getDetalles().isEmpty()) {
            System.out.println("El pedido no tiene productos.");
            return;
        }

        for (DetallePedido detalle : pedido.getDetalles()) {

            Producto producto = buscarProducto(
                detalle.getProducto().getCodigo()
            );

            if (producto == null || detalle.getCantidad() <= 0) {
                System.out.println("Error: producto o cantidad no valida.");
                return;
            }

            int cantidadTotal = 0;

            for (DetallePedido otro : pedido.getDetalles()) {
                if (otro.getProducto().getCodigo() == producto.getCodigo()) {
                    cantidadTotal += otro.getCantidad();
                }
            }

            if (cantidadTotal > producto.getStock()) {
                System.out.println("Error: stock insuficiente.");
                return;
            }
        }

        for (DetallePedido detalle : pedido.getDetalles()) {

            Producto producto = buscarProducto(
                detalle.getProducto().getCodigo()
            );

            producto.setStock(
                producto.getStock() - detalle.getCantidad()
            );
        }

        pedido.calcularTotal();
        pedidos.add(pedido);

        guardarProductos();
        guardarPedidos();

        System.out.println("Pedido registrado correctamente.");
        System.out.printf("Total: $%.2f%n", pedido.getTotal());
    }

    public void generarReporte() {

        double totalVentas = 0;

        System.out.println("\n--- REPORTE DE VENTAS ---");

        for (Pedido pedido : pedidos) {

            System.out.printf(
                "Pedido %d | Total: $%.2f%n",
                pedido.getNumeroPedido(),
                pedido.getTotal()
            );

            totalVentas += pedido.getTotal();
        }

        System.out.println("Pedidos registrados: " + pedidos.size());
        System.out.printf("Total vendido: $%.2f%n", totalVentas);
    }

    public String obtenerReporte() {

        double totalVentas = 0;
        String reporte = "--- REPORTE DE VENTAS ---\n\n";

        for (Pedido pedido : pedidos) {

            reporte += "Pedido N.º " + pedido.getNumeroPedido()
                    + " | Total: $"
                    + String.format("%.2f", pedido.getTotal())
                    + "\n";

            totalVentas += pedido.getTotal();
        }

        reporte += "\nPedidos registrados: " + pedidos.size();
        reporte += "\nTotal vendido: $"
                + String.format("%.2f", totalVentas);

        return reporte;
    }

    public void guardarProductos() {

        try (PrintWriter escritor = new PrintWriter(
                new OutputStreamWriter(
                    new FileOutputStream("productos.txt"),
                    StandardCharsets.UTF_8))) {

            for (Producto producto : productos) {

                escritor.println(
                    producto.getCodigo() + ";" +
                    producto.getNombre() + ";" +
                    producto.getPrecio() + ";" +
                    producto.getStock()
                );
            }

        } catch (IOException e) {
            System.out.println("Error al guardar los productos: "
                    + e.getMessage());
        }
    }

    public void cargarProductos() {

        File archivo = new File("productos.txt");

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(archivo),
                    StandardCharsets.UTF_8))) {

            productos.clear();
            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split(";");

                if (datos.length == 4) {

                    int codigo = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    double precio = Double.parseDouble(datos[2]);
                    int stock = Integer.parseInt(datos[3]);

                    Producto producto = new Producto(
                        codigo, nombre, precio, stock
                    );

                    productos.add(producto);
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar productos: "
                    + e.getMessage());
        }
    }

    public void guardarPedidos() {

        try (PrintWriter escritor = new PrintWriter(
                new OutputStreamWriter(
                    new FileOutputStream("pedidos.txt"),
                    StandardCharsets.UTF_8))) {

            for (Pedido pedido : pedidos) {

                for (DetallePedido detalle : pedido.getDetalles()) {

                    escritor.println(
                        pedido.getNumeroPedido() + ";" +
                        detalle.getProducto().getCodigo() + ";" +
                        detalle.getCantidad() + ";" +
                        detalle.getSubtotal()
                    );
                }
            }

        } catch (IOException e) {
            System.out.println("Error al guardar los pedidos: "
                    + e.getMessage());
        }
    }

    public void cargarPedidos() {

        File archivo = new File("pedidos.txt");

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(archivo),
                    StandardCharsets.UTF_8))) {

            pedidos.clear();
            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split(";");

                if (datos.length != 4) {
                    continue;
                }

                int numero = Integer.parseInt(datos[0]);
                int codigo = Integer.parseInt(datos[1]);
                int cantidad = Integer.parseInt(datos[2]);
                double subtotal = Double.parseDouble(datos[3]);

                Producto producto = buscarProducto(codigo);

                if (producto == null || cantidad <= 0) {
                    continue;
                }

                Pedido pedidoEncontrado = null;

                for (Pedido p : pedidos) {
                    if (p.getNumeroPedido() == numero) {
                        pedidoEncontrado = p;
                        break;
                    }
                }

                if (pedidoEncontrado == null) {
                    pedidoEncontrado = new Pedido(numero);
                    pedidos.add(pedidoEncontrado);
                }

                DetallePedido detalle =
                        new DetallePedido(producto, cantidad, subtotal);

                pedidoEncontrado.agregarDetalle(detalle);
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar pedidos: "
                    + e.getMessage());
        }
    }

    public int obtenerSiguienteNumeroPedido() {

        int mayorNumero = 0;

        for (Pedido pedido : pedidos) {
            if (pedido.getNumeroPedido() > mayorNumero) {
                mayorNumero = pedido.getNumeroPedido();
            }
        }

        return mayorNumero + 1;
    }

    public ArrayList<Producto> obtenerProductos() {
        return new ArrayList<>(productos);
    }
}
