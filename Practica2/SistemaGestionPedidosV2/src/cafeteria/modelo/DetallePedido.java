package cafeteria.modelo;

public class DetallePedido {
    private Producto producto;
    private int cantidad;
    private double subtotal;
    public DetallePedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }
    public DetallePedido(Producto producto, int cantidad, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }
    public Producto getProducto() {
        return producto;
    }
    public int getCantidad() {
        return cantidad;
    }
    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }
    public double getSubtotal() {
        return subtotal;
    }
}
