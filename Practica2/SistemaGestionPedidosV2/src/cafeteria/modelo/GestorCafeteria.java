package cafeteria.modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.*;
import java.time.LocalDate;

public class GestorCafeteria {

    private ArrayList<Producto> productos;
    private ArrayList<Pedido> pedidos;
    private ArrayList<Integer> pedidosSinFecha;
    private ArrayList<Integer> pedidosConIva;

    public GestorCafeteria() {
        productos = new ArrayList<>();
        pedidos = new ArrayList<>();
        pedidosSinFecha = new ArrayList<>();
        pedidosConIva = new ArrayList<>();

        cargarProductos();
        cargarPedidos();
    }

    public void registrarProducto(Producto producto) {

        if (producto == null) {
            return;
        }

        if (buscarProducto(producto.getCodigo()) != null) {
            System.out.println("Error: el codigo ya existe.");
            return;
        }

        if (producto.getNombre() == null
                || producto.getNombre().trim().isEmpty()
                || producto.getPrecio() <= 0
                || producto.getStock() < 0) {

            System.out.println("Datos del producto no validos.");
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

        System.out.println("--- PRODUCTOS ---");

        for (Producto p : productos) {
            System.out.println(
                p.getCodigo() + " - " + p.getNombre()
                + " - $" + p.getPrecio()
                + " - Stock: " + p.getStock()
            );
        }

        String[] nombres = new String[productos.size()];

        for (int i = 0; i < productos.size(); i++) {
            nombres[i] = productos.get(i).getNombre();
        }

        UtilidadesGenericas.mostrarElementos(nombres);

        int cantidad = UtilidadesGenericas.contarElementos(nombres);
        System.out.println("Productos registrados: " + cantidad);
        Integer[] codigos = new Integer[productos.size()];

        for (int i = 0; i < productos.size(); i++) {
            codigos[i] = productos.get(i).getCodigo();
        }

        UtilidadesGenericas.mostrarElementos(codigos);
        UtilidadesGenericas.contarElementos(codigos);
    }

    public Producto buscarProducto(int codigo) {

        for (Producto p : productos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }

        return null;
    }

    public ArrayList<Producto> obtenerProductos() {
        return new ArrayList<>(productos);
    }

    public boolean editarProducto(int codigo, String nombre,
                                  double precio, int stock) {

        Producto producto = buscarProducto(codigo);

        if (producto == null || nombre == null
                || nombre.trim().isEmpty()
                || precio <= 0 || stock < 0) {
            return false;
        }

        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);

        guardarProductos();
        return true;
    }

    public boolean aumentarStock(int codigo, int cantidad) {

        Producto producto = buscarProducto(codigo);

        if (producto == null || cantidad <= 0) {
            return false;
        }

        producto.setStock(producto.getStock() + cantidad);
        guardarProductos();

        return true;
    }

    public void reponerStock(int codigo, int cantidad) {

        if (aumentarStock(codigo, cantidad)) {
            System.out.println("Stock actualizado correctamente.");
        } else {
            System.out.println("No se pudo actualizar el stock.");
        }
    }

    public boolean registrarPedido(Pedido pedido) {

        if (pedido == null || pedido.getDetalles().isEmpty()) {
            return false;
        }

        for (Pedido p : pedidos) {
            if (p.getNumeroPedido() == pedido.getNumeroPedido()) {
                System.out.println("El numero de pedido ya existe.");
                return false;
            }
        }

        for (DetallePedido detalle : pedido.getDetalles()) {

            Producto producto = buscarProducto(
                detalle.getProducto().getCodigo()
            );

            if (producto == null || detalle.getCantidad() <= 0) {
                return false;
            }

            int cantidadTotal = 0;

            for (DetallePedido otro : pedido.getDetalles()) {
                if (otro.getProducto().getCodigo()
                        == producto.getCodigo()) {
                    cantidadTotal += otro.getCantidad();
                }
            }

            if (cantidadTotal > producto.getStock()) {
                System.out.println("Stock insuficiente.");
                return false;
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
        pedidosConIva.add(pedido.getNumeroPedido());

        guardarProductos();
        guardarPedidos();

        System.out.println("Pedido registrado correctamente.");
        System.out.printf(
            "Total a pagar: $%.2f%n",
            pedido.getTotalConIva()
        );

        return true;
    }

    public int obtenerSiguienteNumeroPedido() {

        int mayor = 0;

        for (Pedido pedido : pedidos) {
            if (pedido.getNumeroPedido() > mayor) {
                mayor = pedido.getNumeroPedido();
            }
        }

        return mayor + 1;
    }

    private double totalDelPedido(Pedido pedido) {

        if (pedidosConIva.contains(pedido.getNumeroPedido())) {
            return pedido.getTotalConIva();
        }

        return pedido.getTotal();
    }

    public void generarReporte() {
        System.out.println(obtenerReporte());
    }

    public String obtenerReporte() {

        StringBuilder reporte = new StringBuilder();
        LocalDate hoy = LocalDate.now();

        int cantidadHoy = 0;
        double ventasHoy = 0;
        double ventasGenerales = 0;

        reporte.append("--- VENTAS DE HOY (")
               .append(hoy)
               .append(") ---\n\n");

        for (Pedido pedido : pedidos) {

            if (!pedidosSinFecha.contains(pedido.getNumeroPedido())
                    && hoy.equals(pedido.getFecha())) {

                double total = totalDelPedido(pedido);

                reporte.append("Pedido N. ")
                       .append(pedido.getNumeroPedido())
                       .append(" - $")
                       .append(String.format("%.2f", total))
                       .append("\n");

                cantidadHoy++;
                ventasHoy += total;
            }
        }

        if (cantidadHoy == 0) {
            reporte.append("No hay ventas registradas hoy.\n");
        }

        reporte.append("Pedidos de hoy: ")
               .append(cantidadHoy)
               .append("\n");

        reporte.append(String.format(
            "Total vendido hoy: $%.2f%n", ventasHoy
        ));

        reporte.append("\n--- HISTORIAL GENERAL ---\n\n");

        for (Pedido pedido : pedidos) {

            String fecha;

            if (pedidosSinFecha.contains(pedido.getNumeroPedido())) {
                fecha = "Sin fecha (registro anterior)";
            } else {
                fecha = pedido.getFecha().toString();
            }

            double total = totalDelPedido(pedido);

            reporte.append("Pedido N. ")
                   .append(pedido.getNumeroPedido())
                   .append(" - ")
                   .append(fecha)
                   .append(" - $")
                   .append(String.format("%.2f", total))
                   .append("\n");

            ventasGenerales += total;
        }

        reporte.append("\nPedidos registrados: ")
               .append(pedidos.size())
               .append("\n");

        reporte.append(String.format(
            "Total vendido: $%.2f%n", ventasGenerales
        ));

        reporte.append("\n--- PRODUCTOS MAS VENDIDOS ---\n\n");

        Map<Integer, Integer> cantidades = new LinkedHashMap<>();
        Map<Integer, String> nombres = new LinkedHashMap<>();

        for (Pedido pedido : pedidos) {

            for (DetallePedido detalle : pedido.getDetalles()) {

                int codigo = detalle.getProducto().getCodigo();

                nombres.put(
                    codigo, detalle.getProducto().getNombre()
                );

                int anterior = cantidades.getOrDefault(codigo, 0);

                cantidades.put(
                    codigo, anterior + detalle.getCantidad()
                );
            }
        }

        if (cantidades.isEmpty()) {
            reporte.append("No hay productos vendidos.\n");
        } else {

            ArrayList<Integer> codigos =
                new ArrayList<>(cantidades.keySet());

            codigos.sort((a, b) ->
                Integer.compare(cantidades.get(b), cantidades.get(a))
            );

            for (int codigo : codigos) {

                reporte.append(nombres.get(codigo))
                       .append(" (codigo ")
                       .append(codigo)
                       .append("): ")
                       .append(cantidades.get(codigo))
                       .append(" unidades\n");
            }
        }

        return reporte.toString();
    }

    public void guardarProductos() {

        try (PrintWriter escritor =
                new PrintWriter(new FileWriter("productos.txt"))) {

            for (Producto p : productos) {

                escritor.println(
                    p.getCodigo() + ";"
                    + p.getNombre() + ";"
                    + p.getPrecio() + ";"
                    + p.getStock()
                );
            }

        } catch (IOException e) {
            System.out.println("Error al guardar productos.");
        }
    }

    public void cargarProductos() {

        File archivo = new File("productos.txt");

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader lector =
                new BufferedReader(new FileReader(archivo))) {

            productos.clear();
            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split(";");

                if (datos.length == 4) {

                    Producto producto = new Producto(
                        Integer.parseInt(datos[0]),
                        datos[1],
                        Double.parseDouble(datos[2]),
                        Integer.parseInt(datos[3])
                    );

                    productos.add(producto);
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar productos.");
        }
    }

    public void guardarPedidos() {

        try (PrintWriter escritor =
                new PrintWriter(new FileWriter("pedidos.txt"))) {

            for (Pedido pedido : pedidos) {

                for (DetallePedido detalle : pedido.getDetalles()) {

                    String linea =
                        pedido.getNumeroPedido() + ";"
                        + detalle.getProducto().getCodigo() + ";"
                        + detalle.getCantidad() + ";"
                        + detalle.getSubtotal();

                    if (!pedidosSinFecha.contains(
                            pedido.getNumeroPedido())) {

                        linea += ";" + pedido.getFecha();
                    }

                    if (pedidosConIva.contains(
                            pedido.getNumeroPedido())) {

                        linea += ";IVA15";
                    }

                    escritor.println(linea);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al guardar pedidos.");
        }
    }

    public void cargarPedidos() {

        File archivo = new File("pedidos.txt");

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader lector =
                new BufferedReader(new FileReader(archivo))) {

            pedidos.clear();
            pedidosSinFecha.clear();
            pedidosConIva.clear();

            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split(";");

                if (datos.length < 4 || datos.length > 6) {
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

                Pedido encontrado = null;

                for (Pedido p : pedidos) {
                    if (p.getNumeroPedido() == numero) {
                        encontrado = p;
                        break;
                    }
                }

                if (encontrado == null) {

                    encontrado = new Pedido(numero);

                    if (datos.length >= 5
                            && !datos[4].equals("IVA15")) {

                        encontrado.setFecha(
                            LocalDate.parse(datos[4])
                        );

                    } else {
                        pedidosSinFecha.add(numero);
                    }

                    pedidos.add(encontrado);
                }

                if (datos[datos.length - 1].equals("IVA15")
                        && !pedidosConIva.contains(numero)) {

                    pedidosConIva.add(numero);
                }

                encontrado.agregarDetalle(
                    new DetallePedido(producto, cantidad, subtotal)
                );
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar pedidos.");
        }
    }
}
