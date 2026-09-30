package main;

import dao.ConexionBD;

import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Inicia la interfaz gráfica de SpeedFast.
 */
public class Main {
    /**
     * Muestra la ventana principal desde el hilo de eventos de Swing.
     *
     * @param args argumentos de línea de comandos, no utilizados
     */
    public static void main(String[] args) {
        try (Connection connection = ConexionBD.getConnection()) {
            System.out.println("Conexión ok");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos");
            e.printStackTrace();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }


}
