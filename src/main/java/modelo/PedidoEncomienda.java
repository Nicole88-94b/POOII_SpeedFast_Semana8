package modelo;


import interfaces.Cancelable;
import interfaces.Despachable;


/**
 * Encomienda cuyo embalaje y peso se validan antes de su entrega.
 */
public class PedidoEncomienda extends Pedido implements Despachable, Cancelable {

    private String estadoEmbalaje;
    private double peso;
    private Repartidor repartidor;

    /**
     * Crea una encomienda con su embalaje, peso y repartidor asociado.
     *
     * @param idPedido identificador numérico del pedido
     * @param direccionEntrega dirección de destino
     * @param tipoPedido descripción del pedido
     * @param distanciaKilometros distancia de entrega, mayor que cero
     * @param estadoEmbalaje estado inicial del embalaje
     * @param peso peso de la encomienda en kilogramos
     * @param repartidor repartidor asociado al pedido
     * @throws IllegalArgumentException si la distancia, el embalaje, el peso o el repartidor son inválidos
     */
    public PedidoEncomienda(int idPedido, String direccionEntrega, String tipoPedido, int distanciaKilometros, String estadoEmbalaje,
                            double peso, Repartidor repartidor) {
        super(idPedido, direccionEntrega, tipoPedido, distanciaKilometros);
        setEstadoEmbalaje(estadoEmbalaje);
        setPeso(peso);
        setRepartidor(repartidor);
        actualizarEstadoEmbalaje();

    }

    /**
     * Crea una encomienda pendiente de asignación y define su estado inicial
     * según la revisión del embalaje.
     *
     * @param idPedido identificador numérico del pedido
     * @param direccionEntrega dirección de destino
     * @param tipoPedido descripción del tipo de pedido
     * @param distanciaKilometros distancia de entrega, mayor que cero
     * @param estadoEmbalaje estado inicial del embalaje
     * @param peso peso de la encomienda en kilogramos
     * @throws IllegalArgumentException si la distancia, el embalaje o el peso son inválidos
     */
    public PedidoEncomienda(int idPedido, String direccionEntrega, String tipoPedido, int distanciaKilometros, String estadoEmbalaje,
                            double peso) {
        super(idPedido, direccionEntrega, tipoPedido, distanciaKilometros);
        setEstadoEmbalaje(estadoEmbalaje);
        setPeso(peso);
        actualizarEstadoEmbalaje();
    }

    public String getEstadoEmbalaje() {
        return estadoEmbalaje;
    }

    /**
     * Define si el embalaje fue aceptado o rechazado.
     *
     * @param estadoEmbalaje {@code ACEPTADA} o {@code RECHAZADA}
     * @throws IllegalArgumentException si el valor no coincide con esas opciones
     */
    public void setEstadoEmbalaje(String estadoEmbalaje) throws IllegalArgumentException {
        if (estadoEmbalaje == null || estadoEmbalaje.trim().isEmpty() || !estadoEmbalaje.matches("ACEPTADA|RECHAZADA")) {
            throw new IllegalArgumentException("Estado del embalaje inválido. Ingrese 'ACEPTADA' o 'RECHAZADA'.");
        }
        this.estadoEmbalaje = estadoEmbalaje;
    }

    private void actualizarEstadoEmbalaje () {
        if ("RECHAZADA".equals(estadoEmbalaje)) {
            setEstado(EstadoPedido.RECHAZADO);
        }
    }

    public double getPeso() {
        return peso;
    }

    /**
     * Establece el peso admitido para la encomienda.
     *
     * @param peso peso mayor que cero y de hasta 50 kg
     * @throws IllegalArgumentException si el peso está fuera de ese rango
     */
    public void setPeso(double peso) throws IllegalArgumentException {
        if (peso <= 0 || peso > 50) {
            throw new IllegalArgumentException("Peso inválido. Registre un peso mayor a 0 y menor o igual a 50 kg.");
        }
        this.peso = peso;
    }

    @Override
    public Repartidor getRepartidor() {
        return repartidor;
    }

    /**
     * Cambia el repartidor asociado a la encomienda.
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
        if (estadoEmbalaje.equals("RECHAZADA")) {
            return "Su encomienda ha sido rechazada.";
        }
        return "El repartidor tiene la capacidad de transporte";
    }

    @Override
    public String asignarRepartidor(String nombreRepartidor) {
        if (nombreRepartidor == null || nombreRepartidor.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese un nombre válido.");
        }

        if (!"ACEPTADA".equals(estadoEmbalaje)) {
            return "No se puede asignar el pedido: embalaje rechazado.";
        }
            return "Peso y embalaje verificados. Pedido asignado a " + nombreRepartidor + ".";

    }



    /**
     * Suma 1,5 minutos por kilómetro a los veinte minutos base y redondea el resultado.
     *
     * @return tiempo estimado en minutos
     */
    @Override
    public int calcularTiempoEntrega() {
        int tiempoBaseDelPedido = 20;
        int tiempoEntrega = (int) Math.round(tiempoBaseDelPedido + (getDistanciaKilometros() * 1.5));
        return tiempoEntrega;
    }

    @Override
    public String mostrarResumen(){
        return super.mostrarResumen() +
                "\nEstado del embalaje: " + getEstadoEmbalaje() +
                "\nPeso de la encomienda: " + getPeso() + " kg";
    }

    /**
     * Comprueba que el embalaje esté aceptado y el repartidor disponible.
     * El peso ya fue validado al crear o modificar la encomienda.
     *
     * @return {@code true} si la encomienda puede entrar a la zona de carga
     */
    @Override
    public boolean cumpleRequisitos() {
        return repartidor != null && "ACEPTADA".equals(estadoEmbalaje) && repartidor.isDisponible();
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
        if (repartidor == null || !repartidor.isDisponible() || !"ACEPTADA".equals(estadoEmbalaje)) {
            return false;
        }

        setRepartidor(repartidor);
        return true;
    }

}
