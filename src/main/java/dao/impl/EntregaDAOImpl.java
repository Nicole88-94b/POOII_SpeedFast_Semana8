package dao.impl;

import dao.ConexionBD;
import dao.interfaces.EntregaDAO;
import modelo.Entrega;
import modelo.EntregaResumen;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Registra las entregas que relacionan pedidos y repartidores persistidos.
 */
public class EntregaDAOImpl implements EntregaDAO {

    @Override
    public List<EntregaResumen> readAll() {
        List<EntregaResumen> entregas = new java.util.ArrayList<>();
        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora FROM entrega ORDER BY id";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {
            while (resultado.next()) {
                int idEntrega = resultado.getInt("id");
                int idRepartidor = resultado.getInt("id_repartidor");
                int idPedido = resultado.getInt("id_pedido");
                LocalDate fecha = resultado.getDate("fecha").toLocalDate();
                LocalTime hora = resultado.getTime("hora").toLocalTime();
                EntregaResumen entrega = new EntregaResumen(idEntrega, idPedido, idRepartidor, fecha, hora);
                entregas.add(entrega);

            }
        } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Lo sentimos, hubo un error al listar las entregas.");
            }
        return entregas;
    }

    @Override
    public boolean update(EntregaResumen entrega) {
        if (entrega == null || entrega.getIdEntrega() <= 0) {
            return false;
        }
        String sql = "UPDATE entrega SET fecha = ?, hora = ? WHERE id = ?";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(entrega.getFecha()));
            ps.setTime(2, Time.valueOf(entrega.getHora()));
            ps.setInt(3, entrega.getIdEntrega());

            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al actualizar la entrega.");
            return false;
        }
    }

    @Override
    public boolean delete(int idEntrega) {
        if (idEntrega <= 0) {
            return false;
        }
        String sql = "DELETE FROM entrega WHERE id = ?";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idEntrega);
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

    /**
     * Guarda una entrega y asigna al objeto el identificador generado por MySQL.
     * El pedido y el repartidor deben haber sido almacenados previamente.
     *
     * @param entrega entrega que se desea registrar
     * @return {@code true} si la relación se guardó correctamente
     */
    @Override
    public boolean create(Entrega entrega) {
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

    @Override
    public boolean create(EntregaResumen entrega) {
        if (entrega == null) {
            return false;
        }
        if (entrega.getIdPedido() <= 0 || entrega.getIdRepartidor() <= 0 ||
                entrega.getFecha() == null || entrega.getHora() == null) {
            return false;
        }
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
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
