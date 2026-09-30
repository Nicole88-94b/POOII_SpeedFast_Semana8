package modelo;


import interfaces.Cancelable;
import interfaces.Despachable;

/**
 * Pedido de comida que requiere un repartidor disponible con mochila térmica.
 */
public class PedidoComida extends Pedido implements Despachable, Cancelable {
    private Repartidor repartidor;

    /**
     * Crea un pedido de comida asociado a un repartidor.
     *
     * @param idPedido identificador numérico del pedido
     * @param direccionEntrega dirección de destino
     * @param tipoPedido descripción del pedido
     * @param distanciaKilometros distancia de entrega, mayor que cero
     * @param repartidor repartidor asociado al pedido
     * @throws IllegalArgumentException si la distancia es inválida o el repartidor es nulo
     */
    public PedidoComida(int idPedido, String direccionEntrega, String tipoPedido, int distanciaKilometros,
                         Repartidor repartidor) {
        super(idPedido, direccionEntrega, tipoPedido, distanciaKilometros);
        setRepartidor(repartidor);
    }

    /**
     * Crea un pedido de comida pendiente de asignación.
     *
     * @param idPedido identificador numérico del pedido
     * @param direccionEntrega dirección de destino
     * @param tipoPedido descripción del tipo de pedido
     * @param distanciaKilometros distancia de entrega, mayor que cero
     * @throws IllegalArgumentException si la distancia es inválida
     */
    public PedidoComida(int idPedido, String direccionEntrega, String tipoPedido, int distanciaKilometros) {
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
        if (!repartidor.isTieneMochilaTermica()) {
            return "No es posible asignar un repartidor sin una mochila térmica.";
        }
        return "Repartidor " + repartidor.getNombreRepartidor() + " encontrado.";

    }

    @Override
    public String asignarRepartidor(String nombreRepartidor) throws IllegalArgumentException {
        if (nombreRepartidor == null || nombreRepartidor.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese un nombre de repartidor válido.");
        }
        if (repartidor == null) {
            return "El pedido todavía no tiene un repartidor asignado.";
        }
        boolean verificarMochila = repartidor.isTieneMochilaTermica();
        if (!verificarMochila) {
            return "No se puede asignar a " + nombreRepartidor + ": no tiene mochila térmica.";
        }
        return "Mochila térmica verificada. Pedido asignado a " +
                nombreRepartidor + ".";
    }

    /**
     * Suma dos minutos por kilómetro a los quince minutos base.
     *
     * @return tiempo estimado en minutos
     */
    @Override
    public int calcularTiempoEntrega() {
        int tiempoBaseDelPedido = 15;
        int tiempoEntrega = tiempoBaseDelPedido + (getDistanciaKilometros() * 2);
        return tiempoEntrega;
    }

    @Override
    public String mostrarResumen() {
        String estadoMochila;
        if (repartidor == null) {
            estadoMochila = "Pendiente de asignación.";
        } else {
            estadoMochila = repartidor.isTieneMochilaTermica()
                    ? "Correcto."
                    : "Incorrecto: no tiene mochila térmica.";
        }
        return super.mostrarResumen() +
                "\nEstado de la mochila térmica: " + estadoMochila;
    }

    /**
     * Comprueba que el repartidor esté disponible y tenga mochila térmica.
     *
     * @return {@code true} si el pedido puede entrar a la zona de carga
     */
    @Override
    public boolean cumpleRequisitos() {
        return repartidor != null && repartidor.isTieneMochilaTermica() && repartidor.isDisponible();
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
        if (repartidor == null || !repartidor.isDisponible() || !repartidor.isTieneMochilaTermica()) {
            return false;
        }

        setRepartidor(repartidor);
        return true;
    }

}
