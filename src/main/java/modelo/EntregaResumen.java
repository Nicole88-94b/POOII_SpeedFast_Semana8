package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Contiene los datos persistidos de una entrega sin reconstruir los objetos
 * completos de pedido y repartidor. Se utiliza principalmente en las tablas y
 * formularios de la interfaz gráfica.
 */
public class EntregaResumen {
    private int idEntrega;
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    /**
     * Construye una representación resumida de una entrega.
     *
     * @param idEntrega identificador de la entrega, o cero antes de guardarla
     * @param idPedido identificador del pedido asociado
     * @param idRepartidor identificador del repartidor asociado
     * @param fecha fecha registrada para la entrega
     * @param hora hora registrada para la entrega
     */
    public EntregaResumen(int idEntrega, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.idEntrega = idEntrega;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    /**
     * Asigna el identificador generado por MySQL después del registro.
     *
     * @param idEntrega identificador generado
     */
    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    /**
     * @return identificador persistido de la entrega
     */
    public int getIdEntrega() {
        return idEntrega;
    }

    /**
     * @return identificador del pedido asociado
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * @return identificador del repartidor asociado
     */
    public int getIdRepartidor() {
        return idRepartidor;
    }

    /**
     * @return fecha registrada para la entrega
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * @return hora registrada para la entrega
     */
    public LocalTime getHora() {
        return hora;
    }
}
