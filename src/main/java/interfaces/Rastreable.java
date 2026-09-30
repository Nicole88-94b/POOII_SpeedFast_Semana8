package interfaces;

import  java.util.List;

/**
 * Permite consultar los eventos registrados durante la gestión de pedidos.
 */
public interface Rastreable {
    /**
     * Obtiene los mensajes que describen las operaciones registradas.
     *
     * @return lista de eventos del historial
     */
    public List<String> verHistorial();
}
