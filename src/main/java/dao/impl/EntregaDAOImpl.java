package dao.impl;

import dao.ConexionBD;
import modelo.Entrega;

import java.sql.*;

/**
 * Registra las entregas que relacionan pedidos y repartidores persistidos.
 */
public class EntregaDAOImpl {

    /**
     * Guarda una entrega y asigna al objeto el identificador generado por MySQL.
     * El pedido y el repartidor deben haber sido almacenados previamente.
     *
     * @param entrega entrega que se desea registrar
     * @return {@code true} si la relación se guardó correctamente
     */
    public boolean guardar(Entrega entrega) {
        if (entrega == null) {
            return false;
        }
        if (entrega.getPedido().getIdPedido() <= 0 || entrega.getRepartidor().getIdRepartidor() <= 0) {
            return false;
        }
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entrega.getPedido().getIdPedido());
            ps.setInt(2, entrega.getRepartidor().getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora()));

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas == 0) {
                return false;
            }
            try (ResultSet clavesGeneradas = ps.getGeneratedKeys()) {
                if (clavesGeneradas.next()) {
                    int idGenerado = clavesGeneradas.getInt(1);
                    entrega.setIdEntrega(idGenerado);
                    return true;
                }
            }
            return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al guardar la entrega.");
            return false;
        }
    }
}
