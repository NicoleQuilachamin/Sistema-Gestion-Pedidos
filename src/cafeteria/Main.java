package cafeteria;

import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static GestorCafeteria gestor = new GestorCafeteria();
    private static int siguientePedido = gestor.obtenerSiguienteNumeroPedido();

    public static void main(String[] args) {

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    registrarProducto();
                    break;
                case 2:
                    gestor.listarProductos();
                    break;
                case 3:
                    reponerStock();
                    break;
                case 4:
                    registrarPedido();
                    break;
                case 5:
                    gestor.generarReporte();
                    break;
                case 6:
                    System.out.println("Gracias por usar El Buen Sabor.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 6);

        scanner.close();
    }

    public static void mostrarMenu() {

        System.out.println("\n===== EL BUEN SABOR =====");
        System.out.println("1. Registrar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Reponer stock");
        System.out.println("4. Registrar pedido");
        System.out.println("5. Reporte de ventas");
        System.out.println("6. Salir");
    }

    private static int leerEntero(String mensaje) {

        while (true) {
            System.out.print(mensaje);

            try {
                return Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {

        while (true) {
            System.out.print(mensaje);

            try {
                return Double.parseDouble(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Ingrese un precio valido. Use punto decimal.");
            }
        }
    }

    private static void registrarProducto() {

        int codigo = leerEntero("Codigo: ");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        double precio = leerDouble("Precio: ");
        int stock = leerEntero("Stock inicial: ");

        if (codigo <= 0 || nombre.trim().isEmpty()) {
            System.out.println("Codigo o nombre no valido.");
            return;
        }

        Producto producto = new Producto(codigo, nombre, precio, stock);
        gestor.registrarProducto(producto);
    }

    private static void reponerStock() {

        int codigo = leerEntero("Codigo del producto: ");
        int cantidad = leerEntero("Cantidad a reponer: ");

        gestor.reponerStock(codigo, cantidad);
    }

    private static void registrarPedido() {

        Pedido pedido = new Pedido(siguientePedido);

        while (true) {

            int codigo = leerEntero("Codigo del producto (0 para terminar): ");

            if (codigo == 0) {
                break;
            }

            Producto producto = gestor.buscarProducto(codigo);

            if (producto == null) {
                System.out.println("Producto no encontrado.");
                continue;
            }

            int cantidad = leerEntero("Cantidad: ");

            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser mayor que cero.");
                continue;
            }

            int cantidadAcumulada = cantidad;

            for (DetallePedido detalle : pedido.getDetalles()) {
                if (detalle.getProducto().getCodigo() == codigo) {
                    cantidadAcumulada += detalle.getCantidad();
                }
            }

            if (cantidadAcumulada > producto.getStock()) {
                System.out.println("Stock insuficiente.");
                continue;
            }

            DetallePedido detalle = new DetallePedido(producto, cantidad);
            pedido.agregarDetalle(detalle);

            System.out.printf("Subtotal: $%.2f%n", detalle.getSubtotal());
        }

        if (pedido.getDetalles().isEmpty()) {
            System.out.println("No se registro ningun producto.");
            return;
        }

        gestor.registrarPedido(pedido);
        siguientePedido++;
    }
}
