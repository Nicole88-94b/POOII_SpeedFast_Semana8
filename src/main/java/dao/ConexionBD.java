package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza los parámetros y la apertura de conexiones con la base de datos
 * local de SpeedFast.
 */
public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("SPEEDFAST_DB_PASSWORD");

    /**
     * Abre una conexión utilizando la contraseña definida en la variable de
     * entorno {@code SPEEDFAST_DB_PASSWORD}.
     *
     * @return conexión activa con {@code speedfast_db}
     * @throws SQLException si MySQL no está disponible o las credenciales son inválidas
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
