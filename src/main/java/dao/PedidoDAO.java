package dao;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoResumen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Realiza las operaciones de persistencia y consulta de pedidos.
 */
public class PedidoDAO {

    /**
     * Guarda un pedido y asigna al objeto el identificador generado por MySQL.
     *
     * @param pedido pedido que se desea almacenar
     * @return {@code true} si el registro se guardó y recibió un identificador
     */
    public boolean guardar(Pedido pedido) {
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
    public List<PedidoResumen> listarTodos() {
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
}
