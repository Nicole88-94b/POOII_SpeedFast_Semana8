package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa el registro persistente de un pedido realizado por un repartidor
 * en una fecha y hora determinadas.
 */
public class Entrega {
    private int idEntrega;
    private Pedido pedido;
    private Repartidor repartidor;
    private LocalDate fecha;
    private LocalTime hora;

    /**
     * Crea una entrega pendiente de recibir su identificador desde MySQL.
     *
     * @param pedido pedido entregado
     * @param repartidor repartidor responsable
     * @param fecha fecha de la entrega
     * @param hora hora de la entrega
     * @throws IllegalArgumentException si alguno de los datos es nulo
     */
    public Entrega(Pedido pedido, Repartidor repartidor, LocalDate fecha, LocalTime hora) {
        setPedido(pedido);
        setRepartidor(repartidor);
        setFecha(fecha);
        setHora(hora);
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) throws IllegalArgumentException {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido inválido. Registre un nuevo pedido");
        }
        this.pedido = pedido;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) throws IllegalArgumentException {
        if (repartidor == null) {
            throw new IllegalArgumentException(
                    "Repartidor inválido. Registre un repartidor.");
        }
        this.repartidor = repartidor;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        if (idEntrega < 0) {
            throw new IllegalArgumentException("El identificador no puede ser negativo.");
        }
        this.idEntrega = idEntrega;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) throws IllegalArgumentException {
        if (fecha == null) {
            throw new IllegalArgumentException("Fecha inválida. Registre una nueva fecha");
        }
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) throws IllegalArgumentException {
        if (hora == null) {
            throw new IllegalArgumentException("Hora inválida. Registre una hora");
        }
        this.hora = hora;
    }
}

