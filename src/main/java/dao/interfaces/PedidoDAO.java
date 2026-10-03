package dao.interfaces;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoResumen;

import java.util.List;

/**
 * Define las operaciones de persistencia disponibles para los pedidos.
 * También incluye las actualizaciones utilizadas durante la asignación y el
 * avance de una entrega.
 */
public interface PedidoDAO {
    /**
     * Registra un pedido y recupera el identificador generado por MySQL.
     *
     * @param pedido pedido que se desea almacenar
     * @return {@code true} si el registro fue creado correctamente
     */
    boolean create(Pedido pedido);

    /**
     * Recupera los datos comunes de todos los pedidos registrados.
     *
     * @return lista de pedidos resumidos
     */
    List<PedidoResumen> readAll();

    /**
     * Actualiza los datos editables de un pedido existente.
     *
     * @param pedido resumen con los datos actualizados
     * @return {@code true} si MySQL modificó el registro
     */
    boolean update(PedidoResumen pedido);

    /**
     * Elimina un pedido mediante su identificador.
     *
     * @param idPedido identificador persistido del pedido
     * @return {@code true} si el registro fue eliminado
     */
    boolean delete(int idPedido);

    /**
     * Persiste la relación entre un pedido y el repartidor seleccionado.
     *
     * @param idPedido identificador del pedido
     * @param idRepartidor identificador del repartidor
     * @return {@code true} si la asignación fue almacenada
     */
    boolean asignarRepartidor(int idPedido, int idRepartidor);

    /**
     * Guarda una transición de estado realizada durante la simulación.
     *
     * @param idPedido identificador del pedido
     * @param estado nuevo estado del pedido
     * @return {@code true} si el estado fue actualizado
     */
    boolean updateEstado(int idPedido, EstadoPedido estado);
}
