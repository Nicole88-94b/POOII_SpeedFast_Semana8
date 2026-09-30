package interfaces;

/**
 * Define el despacho de un pedido que ya fue reservado.
 */
public interface Despachable {
    /**
     * Intenta despachar el pedido.
     *
     * @return {@code true} si el pedido quedó despachado
     */
    public boolean despachar();
}
