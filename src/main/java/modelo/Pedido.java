package modelo;

/**
 * Reúne los datos y comportamientos comunes de los pedidos de SpeedFast.
 * Cada tipo de pedido define sus propias reglas de asignación y reserva.
 */
public abstract class Pedido {
    private int idPedido;
    private String direccionEntrega;
    private String tipoPedido;
    private int distanciaKilometros;
    private boolean despachado;
    private boolean reservado;
    private boolean cancelado;
    private EstadoPedido estado;

    /**
     * Crea un pedido pendiente de entrega.
     *
     * @param idPedido identificador numérico del pedido
     * @param direccionEntrega dirección donde se realizará la entrega
     * @param tipoPedido descripción del tipo de pedido
     * @param distanciaKilometros distancia de entrega, mayor que cero
     * @throws IllegalArgumentException si la distancia no es positiva
     */
    public Pedido(int idPedido, String direccionEntrega, String tipoPedido,
                  int distanciaKilometros) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
        setDistanciaKilometros(distanciaKilometros);
        //Estado inicial por defecto del pedido
        this.despachado = false;
        this.reservado = false;
        this.cancelado = false;
        //Estado de la entrega
        this.estado = EstadoPedido.PENDIENTE;

    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public int getDistanciaKilometros() {
        return distanciaKilometros;
    }

    /**
     * Actualiza la distancia utilizada para estimar el tiempo de entrega.
     *
     * @param distanciaKilometros nueva distancia, mayor que cero
     * @throws IllegalArgumentException si la distancia no es positiva
     */
    public void setDistanciaKilometros(int distanciaKilometros) throws IllegalArgumentException {

        if (distanciaKilometros <= 0) {
            throw new IllegalArgumentException("La distancia debe ser superior a 0.");
        }
        this.distanciaKilometros = distanciaKilometros;
    }

    public boolean isDespachado() {
        return despachado;
    }

    protected void setDespachado() {
        despachado = true;
    }

    public boolean isReservado() {
        return reservado;
    }

    protected void setReservado() {
        reservado = true;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    protected void setCancelado() {
        cancelado = true;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado de la entrega desde las clases del .modelo.
     *
     * @param estado nuevo estado del pedido
     */
    protected void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el repartidor asociado al pedido.
     *
     * @return repartidor asignado
     */
    public abstract Repartidor getRepartidor();

    @Override
    public String toString() {
        return
                "\n----------------Resumen de su pedido----------------" +
                "\nID del pedido: " + idPedido +
                "\nDirección de la Entrega: " + direccionEntrega +
                "\nTipo de Pedido: " + tipoPedido +
                "\nDistancia: " + distanciaKilometros + " km" +
                "\nTiempo de entrega estimado: " + calcularTiempoEntrega() + " minutos" +
                "\nEstado: " + estado;

    }

    /**
     * Describe el resultado de la asignación automática del repartidor.
     *
     * @return mensaje sobre la asignación
     */
    public String asignarRepartidor() {
        return "Espere mientras se asigna un repartidor...";
    }

    /**
     * Describe la asignación usando el nombre indicado.
     *
     * @param nombreRepartidor nombre que se mostrará en el resultado
     * @return mensaje sobre la asignación
     */
    public abstract String asignarRepartidor(String nombreRepartidor);

    /**
     * Calcula el tiempo estimado según el tipo de pedido.
     *
     * @return tiempo estimado en minutos
     */
    public abstract int calcularTiempoEntrega();

    /**
     * Prepara los datos del pedido para mostrarlos en consola.
     *
     * @return resumen del pedido y su estado de entrega
     */
    public String mostrarResumen() {
        return toString();
    }

    /**
     * Comprueba las condiciones necesarias antes de admitir el pedido en la zona de carga.
     *
     * @return {@code true} si el pedido puede ingresarse a la cola
     */
    public abstract boolean cumpleRequisitos();

    /**
     * Intenta reservar el pedido según las reglas de su tipo y su estado actual.
     *
     * @return {@code true} si el pedido quedó reservado
     */
    public abstract boolean reservar();

    /**
     * Intenta asociar un repartidor aplicando las reglas particulares del pedido.
     *
     * @param repartidor repartidor que se desea asignar
     * @return {@code true} si el repartidor cumple las condiciones requeridas
     */
    public abstract boolean asignarRepartidor(Repartidor repartidor);
}
