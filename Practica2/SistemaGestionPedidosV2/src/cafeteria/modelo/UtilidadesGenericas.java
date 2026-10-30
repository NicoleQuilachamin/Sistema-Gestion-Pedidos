
package cafeteria.modelo;

public class UtilidadesGenericas {

    public static <T> void mostrarElementos(T... elementos) {

        for (T elemento : elementos) {
            System.out.println(elemento);
        }
    }

    public static <T> int contarElementos(T... elementos) {

        int cantidad = 0;

        for (T elemento : elementos) {
            if (elemento != null) {
                cantidad++;
            }
        }

        return cantidad;
    }
}
