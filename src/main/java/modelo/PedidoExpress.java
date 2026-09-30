package modelo;

import interfaces.Cancelable;
import interfaces.Despachable;

/**
 * Pedido express que requiere un repartidor disponible para su entrega.
 */
public class PedidoExpress extends Pedido implements Cancelable, Despachable {
    private Repartidor repartidor;

    /**
     * Crea un pedido express asociado a un repartidor.
     *
     * @param idPedido identificador numérico del pedido
     * @param direccionEntrega dirección de destino
     * @param tipoPedido descripción del pedido
     * @param distanciaKilometros distancia de entrega, mayor que cero
     * @param repartidor repartidor asociado al pedido
     * @throws IllegalArgumentException si la distancia es inválida o el repartidor es nulo
     */
    public PedidoExpress(int idPedido, String direccionEntrega, String tipoPedido, int distanciaKilometros,
                          Repartidor repartidor) {
        super(idPedido, direccionEntrega, tipoPedido, distanciaKilometros);
        setRepartidor(repartidor);
    }

    /**
     * Crea un pedido express pendiente de asignación.
     *
     * @param idPedido identificador numérico del pedido
     * @param direccionEntrega dirección de destino
     * @param tipoPedido descripción del tipo de pedido
     * @param distanciaKilometros distancia de entrega, mayor que cero
     * @throws IllegalArgumentException si la distancia es inválida
     */
    public PedidoExpress(int idPedido, String direccionEntrega, String tipoPedido, int distanciaKilometros) {
        super(idPedido, direccionEntrega, tipoPedido, distanciaKilometros);
    }

    @Override
    public Repartidor getRepartidor() {
        return repartidor;
    }

    /**
     * Cambia el repartidor asociado al pedido.
     *
     * @param repartidor nuevo repartidor
     * @throws IllegalArgumentException si el repartidor es nulo
     */
    public void setRepartidor(Repartidor repartidor) throws IllegalArgumentException {
        if (repartidor == null) {
            throw new IllegalArgumentException("Repartidor inválido");
        }
        this.repartidor = repartidor;
    }

    @Override
    public String asignarRepartidor() {
        if (repartidor == null) {
            return "El pedido todavía no tiene un repartidor asignado.";
        }
        if (!repartidor.isDisponible()) {
            return "Repartidor ocupado.";
        }
        return "Se ha verificado disponibilidad del repartidor. \nRepartidor asignado.";
    }

    @Override
    public String asignarRepartidor(String nombreRepartidor) throws IllegalArgumentException {
        if (nombreRepartidor == null || nombreRepartidor.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese un nombre de repartidor válido.");
        }
        if (repartidor == null) {
            return "El pedido todavía no tiene un repartidor asignado.";
        }
        if (!repartidor.isDisponible()) {
            return "No se puede asignar a " + nombreRepartidor + ": el repartidor está ocupado.";
        }

        return "Repartidor cercano con disponibilidad inmediata encontrado. " +
                "Pedido asignado a " + nombreRepartidor + ".";
    }

    /**
     * Estima diez minutos y añade cinco si la distancia supera cinco kilómetros.
     *
     * @return tiempo estimado en minutos
     */
    @Override
    public int calcularTiempoEntrega() {
        int tiempoBaseDelPedido = 10;
        int tiempoEntrega = tiempoBaseDelPedido;
        if (getDistanciaKilometros() > 5) {
            tiempoEntrega = tiempoBaseDelPedido + 5;
        }
        return tiempoEntrega;
    }

    @Override
    public String mostrarResumen() {
        return super.mostrarResumen();
    }

    /**
     * Comprueba la disponibilidad del repartidor asociado.
     *
     * @return {@code true} si el pedido puede entrar a la zona de carga
     */
    @Override
    public boolean cumpleRequisitos() {
        return repartidor != null && repartidor.isDisponible();
    }

    @Override
    public boolean reservar() {
        if (!cumpleRequisitos()) {
            return false;
        }
        if (isReservado() || isDespachado() || isCancelado()) {
            return false;
        }
        setReservado();
        return true;
    }

    @Override
    public boolean despachar() {
        if (!isReservado() || isDespachado() || isCancelado()) {
            return false;
        }

        setDespachado();
        return true;
    }

    @Override
    public boolean cancelar() {
        if (isDespachado() || isCancelado()) {
            return false;
        }
        setCancelado();
        return true;
    }

    @Override
    public boolean asignarRepartidor(Repartidor repartidor) {
        if (repartidor == null || !repartidor.isDisponible()) {
            return false;
        }

        setRepartidor(repartidor);
        return true;
    }

}
