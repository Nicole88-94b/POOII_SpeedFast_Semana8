package interfaces;

/**
 * Define la posibilidad de cancelar un pedido según su estado.
 */
public interface Cancelable {
    /**
     * Intenta cancelar el pedido.
     *
     * @return {@code true} si el pedido quedó cancelado
     */
    public boolean cancelar();
}
