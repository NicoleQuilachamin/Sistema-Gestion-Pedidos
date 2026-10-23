package cafeteria;

import java.util.ArrayList;

public class Pedido {

    private int numeroPedido;
    private ArrayList<DetallePedido> detalles;
    private double total;

    public Pedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
        this.detalles = new ArrayList<>();
        this.total = 0;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public ArrayList<DetallePedido> getDetalles() {
        return detalles;
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        calcularTotal();
    }

    public void calcularTotal() {
        total = 0;

        for (DetallePedido detalle : detalles) {
            total += detalle.getSubtotal();
        }
    }

    public double getTotal() {
        return total;
    }
}
