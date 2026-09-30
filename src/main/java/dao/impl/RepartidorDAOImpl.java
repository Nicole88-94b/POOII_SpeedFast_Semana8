package dao.impl;

import dao.ConexionBD;
import modelo.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el almacenamiento y la consulta de repartidores en MySQL.
 */
public class RepartidorDAOImpl {

    /**
     * Recupera todos los repartidores registrados.
     *
     * @return lista de repartidores ordenada por identificador
     */
    public List<Repartidor> listarTodos() {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidor ORDER BY id";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                Repartidor repartidor = new Repartidor(id, nombre);
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
    public boolean guardar(Repartidor repartidor) {
        if (repartidor == null) {
            return false;
        }
        String sql = "INSERT INTO repartidor(nombre) VALUES (?)";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, repartidor.getNombreRepartidor());

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

}
