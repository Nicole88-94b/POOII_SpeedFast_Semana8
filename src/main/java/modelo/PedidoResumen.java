package modelo;

/**
 * Contiene los datos comunes de un pedido recuperado desde MySQL para su
 * presentación en la tabla de la interfaz.
 */
public class PedidoResumen {
    private int idPedido;
    private String direccion;
    private String tipo;
    private int distanciaKm;
    private EstadoPedido estado;

    /**
     * Construye una vista resumida de un registro de la tabla pedido.
     *
     * @param idPedido identificador persistido
     * @param direccion dirección de entrega
     * @param tipo tipo de pedido
     * @param distanciaKm distancia registrada en kilómetros
     * @param estado estado actual del pedido
     */
    public PedidoResumen(int idPedido, String direccion, String tipo, int distanciaKm, EstadoPedido estado) {
        this.idPedido = idPedido;
        this.direccion = direccion;
        this.tipo = tipo;
        this.distanciaKm = distanciaKm;
        this.estado = estado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public int getDistanciaKm() {
        return distanciaKm;
    }


    public EstadoPedido getEstado() {
        return estado;
    }

}
