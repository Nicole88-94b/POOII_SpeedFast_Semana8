package dao.impl;

import dao.ConexionBD;
import dao.PedidoDAO;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoResumen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Realiza las operaciones de persistencia y consulta de pedidos.
 */
public class PedidoDAOImpl implements PedidoDAO {

    /**
     * Guarda un pedido y asigna al objeto el identificador generado por MySQL.
     *
     * @param pedido pedido que se desea almacenar
     * @return {@code true} si el registro se guardó y recibió un identificador
     */
    @Override
    public boolean create(Pedido pedido) {
        if (pedido == null) {
            return false;
        }
        String sql = "INSERT INTO pedido (direccion, tipo, distancia_km, estado) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pedido.getDireccionEntrega());
            ps.setString(2, pedido.getTipoPedido().toUpperCase());
            ps.setInt(3, pedido.getDistanciaKilometros());
            ps.setString(4, pedido.getEstado().name());

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas == 0) {
                return false;
            }
            try (ResultSet clavesGeneradas = ps.getGeneratedKeys()) {
                if (clavesGeneradas.next()) {
                    int idGenerado = clavesGeneradas.getInt(1);
                    pedido.setIdPedido(idGenerado);
                    return true;
                }
            }
            return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al guardar el pedido");
            return false;
        }
    }

    /**
     * Recupera los datos comunes de todos los pedidos almacenados.
     *
     * @return lista de pedidos ordenada por identificador
     */
    @Override
    public List<PedidoResumen> readAll() {
        List<PedidoResumen> pedidos = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, distancia_km, estado FROM pedido ORDER BY id";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String direccion = resultado.getString("direccion");
                String tipo = resultado.getString("tipo");
                int distancia = resultado.getInt("distancia_km");
                EstadoPedido estado = EstadoPedido.valueOf(resultado.getString("estado"));

                PedidoResumen resumen = new PedidoResumen(id, direccion, tipo, distancia, estado);
                pedidos.add(resumen);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al listar los pedidos");

        }
        return pedidos;

    }

    @Override
    public boolean update(Pedido pedido) {
        if (pedido == null || pedido.getIdPedido() <= 0) {
            return false;
        }
        String sql = "UPDATE pedido SET direccion = ?, tipo = ?, distancia_km = ?, estado = ? WHERE id = ?";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, pedido.getDireccionEntrega());
            ps.setString(2, pedido.getTipoPedido());
            ps.setInt(3, pedido.getDistanciaKilometros());
            ps.setString(4, pedido.getEstado().name());
            ps.setInt(5, pedido.getIdPedido());

            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al actualizar el pedido");
            return false;
        }
    }

    @Override
    public boolean delete(int idPedido) {
        if (idPedido <= 0) {
            return false;
        }
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            int filasEliminadas = ps.executeUpdate();
            if (filasEliminadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al eliminar el registro.");
            return false;
        }
    }

    @Override
    public boolean asignarRepartidor(int idPedido, int idRepartidor) {
        if (idPedido <= 0 || idRepartidor <= 0) {
            return false;
        }
        String sql = "UPDATE pedido SET id_repartidor = ? WHERE id = ? AND id_repartidor IS NULL";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idRepartidor);
            ps.setInt(2, idPedido);

            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al asignar el repartidor al pedido");
            return false;
        }
    }

    @Override
    public boolean updateEstado(int idPedido, EstadoPedido estado) {
        if (idPedido <= 0 || estado == null) {
            return false;
        }
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setInt(2, idPedido);

            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al modificar el estado del pedido");
            return false;
        }
    }
}
