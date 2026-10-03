package dao.interfaces;

import modelo.Entrega;
import modelo.EntregaResumen;

import java.util.List;

/**
 * Define las operaciones CRUD para las entregas y sus relaciones con pedidos
 * y repartidores.
 */
public interface EntregaDAO {
    /**
     * Registra la entrega generada por la simulación y asigna su ID al objeto.
     *
     * @param entrega entrega construida desde el modelo del sistema
     * @return {@code true} si el registro fue creado
     */
    boolean create(Entrega entrega);

    /**
     * Recupera todas las entregas en un formato apropiado para la interfaz.
     *
     * @return lista resumida de entregas
     */
    List<EntregaResumen> readAll();

    /**
     * Corrige la fecha y la hora de una entrega existente.
     *
     * @param entrega entrega con los valores actualizados
     * @return {@code true} si el registro fue modificado
     */
    boolean update(EntregaResumen entrega);

    /**
     * Elimina una entrega mediante su identificador.
     *
     * @param idEntrega identificador persistido de la entrega
     * @return {@code true} si el registro fue eliminado
     */
    boolean delete(int idEntrega);

    /**
     * Registra una entrega creada manualmente desde la interfaz gráfica.
     *
     * @param entrega resumen con los identificadores, fecha y hora
     * @return {@code true} si el registro fue creado
     */
    boolean create(EntregaResumen entrega);
}
