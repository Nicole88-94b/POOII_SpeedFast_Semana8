package dao.interfaces;


import modelo.Repartidor;

import java.util.List;

/**
 * Define las operaciones CRUD utilizadas para administrar repartidores.
 */
public interface RepartidorDAO {
    /**
     * Registra un repartidor y recupera el identificador generado por MySQL.
     *
     * @param repartidor repartidor que se desea almacenar
     * @return {@code true} si el registro fue creado
     */
    boolean create(Repartidor repartidor);

    /**
     * Recupera todos los repartidores almacenados.
     *
     * @return lista de repartidores persistidos
     */
    List<Repartidor> readAll();

    /**
     * Actualiza el nombre, la mochila térmica y la disponibilidad.
     *
     * @param repartidor repartidor con los datos modificados
     * @return {@code true} si el registro fue actualizado
     */
    boolean update(Repartidor repartidor);

    /**
     * Elimina un repartidor que no se encuentre asociado a otros registros.
     *
     * @param idRepartidor identificador persistido del repartidor
     * @return {@code true} si el registro fue eliminado
     */
    boolean delete(int idRepartidor);
}
