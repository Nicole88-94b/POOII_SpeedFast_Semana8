package gestor;

import modelo.Pedido;
import modelo.Repartidor;

import  java.util.concurrent.BlockingQueue;
import  java.util.concurrent.LinkedBlockingQueue;

/**
 * Mantiene una cola compartida de pedidos listos para repartir.
 * Sus operaciones sincronizadas impiden que dos repartidores retiren el mismo pedido.
 */
public class ZonaDeCarga {
    private BlockingQueue<Pedido> pedidos;

    public ZonaDeCarga(){
        this.pedidos = new LinkedBlockingQueue<>();
    }

    /**
     * Agrega un pedido si cumple las condiciones de su tipo y repartidor.
     *
     * @param pedido pedido que se intenta ingresar a la cola
     * @throws IllegalArgumentException si el pedido no cumple los requisitos de entrega
     */
    public synchronized void agregarPedido(Pedido pedido){
        if (!pedido.cumpleRequisitos()) {
            throw new IllegalArgumentException("Lo sentimos, pedido " + pedido.getIdPedido() +
                    " no cumple los requisitos para entrega.");
        }
        pedidos.add(pedido);
    }

    /**
     * Retira el primer pedido asociado al repartidor indicado.
     * La búsqueda y el retiro ocurren bajo el mismo bloqueo para evitar duplicados.
     *
     * @param repartidor repartidor que solicita el siguiente pedido
     * @return pedido retirado, o {@code null} si no hay uno asignado a él
     */
    public synchronized Pedido retirarPedido(Repartidor repartidor) {
        for (Pedido pedido : pedidos){
            if (pedido.getRepartidor() == repartidor){
                pedidos.remove(pedido);
                return pedido;
            }
        }
        return null;
    }

}
