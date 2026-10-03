package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class EntregaResumen {
    private int idEntrega;
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    public EntregaResumen(int idEntrega, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.idEntrega = idEntrega;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
}
