package dao.impl;

import dao.ConexionBD;
import dao.RepartidorDAO;
import modelo.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el almacenamiento y la consulta de repartidores en MySQL.
 */
public class RepartidorDAOImpl implements RepartidorDAO {

    /**
     * Recupera todos los repartidores registrados.
     *
     * @return lista de repartidores ordenada por identificador
     */
    @Override
    public List<Repartidor> readAll() {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT id, nombre, tiene_mochila_termica, disponible FROM repartidor ORDER BY id";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                boolean tieneMochilaTermica = resultado.getBoolean("tiene_mochila_termica");
                boolean disponible = resultado.getBoolean("disponible");
                Repartidor repartidor = new Repartidor(id, nombre, tieneMochilaTermica, disponible);
                repartidores.add(repartidor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al listar los repartidores");

        }
        return repartidores;
    }

    /**
     * Guarda un repartidor y actualiza su identificador con el valor generado
     * por MySQL.
     *
     * @param repartidor repartidor que se desea almacenar
     * @return {@code true} si el registro fue creado correctamente
     */
    @Override
    public boolean create(Repartidor repartidor) {
        if (repartidor == null) {
            return false;
        }
        String sql = "INSERT INTO repartidor(nombre, tiene_mochila_termica, disponible) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, repartidor.getNombreRepartidor());
            ps.setBoolean(2, repartidor.isTieneMochilaTermica());
            ps.setBoolean(3, repartidor.isDisponible());

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas == 0) {
                return false;
            }
            try (ResultSet clavesGeneradas = ps.getGeneratedKeys()) {
                if (clavesGeneradas.next()) {
                    int idGenerado = clavesGeneradas.getInt(1);
                    repartidor.setIdRepartidor(idGenerado);
                    return true;
                }
            }
            return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al guardar el repartidor");
            return false;
        }
    }

    @Override
    public boolean update(Repartidor repartidor) {
        if (repartidor == null || repartidor.getIdRepartidor() <= 0) {
            return false;
        }
        String sql = "UPDATE repartidor SET nombre = ?, tiene_mochila_termica = ?, disponible = ? WHERE id = ?";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, repartidor.getNombreRepartidor());
            ps.setBoolean(2, repartidor.isTieneMochilaTermica());
            ps.setBoolean(3, repartidor.isDisponible());
            ps.setInt(4, repartidor.getIdRepartidor());
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lo sentimos, hubo un error al actualizar el repartidor");
            return false;
        }
    }

    @Override
    public boolean delete(int idRepartidor) {
        if (idRepartidor <= 0) {
            return false;
        }
        String sql = "DELETE FROM repartidor WHERE id = ?";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idRepartidor);
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
}
