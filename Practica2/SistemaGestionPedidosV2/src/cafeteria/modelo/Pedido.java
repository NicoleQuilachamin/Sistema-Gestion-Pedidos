
package cafeteria.modelo;

import java.util.ArrayList;
import java.time.LocalDate;

public class Pedido {

    private int numeroPedido;
    private ArrayList<DetallePedido> detalles;
    private double total;
    private LocalDate fecha;

    public Pedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
        this.detalles = new ArrayList<>();
        this.total = 0;
        this.fecha = LocalDate.now();
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public ArrayList<DetallePedido> getDetalles() {
        return detalles;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    public double getSubtotal() {
        return total;
    }

    public double getIva() {
        return total * 0.15;
    }

    public double getTotalConIva() {
        return total + getIva();
    }

    public double getTotal() {
        return total;
    }
}
