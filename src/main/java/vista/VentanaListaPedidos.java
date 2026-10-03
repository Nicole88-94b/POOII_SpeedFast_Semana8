package vista;

import dao.impl.PedidoDAOImpl;
import gestor.ControladorDeEnvios;
import modelo.EstadoPedido;
import modelo.PedidoResumen;

import  javax.swing.*;
import  javax.swing.table.DefaultTableModel;
import  java.awt.*;

/**
 * Consulta MySQL y presenta los pedidos persistidos en una tabla actualizable.
 */
public class VentanaListaPedidos extends JFrame {
    private final PedidoDAOImpl pedidoDAOImpl = new PedidoDAOImpl();
    private DefaultTableModel modeloTabla;
    private JTable tablaPedidos;
    private final ControladorDeEnvios controlador;


    /**
     * Crea la ventana y carga los pedidos disponibles en la base de datos.
     */
    public VentanaListaPedidos(ControladorDeEnvios controlador) {
        this.controlador = controlador;
        arquitecturaVentana();
    }

    private void arquitecturaVentana() {
        tamanoVentana();
        configuracionPanel();
        setLocationRelativeTo(null);
    }

    private void tamanoVentana() {
        setTitle("Lista de Pedidos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 400);
        setLocation(500, 300);
        getContentPane().setBackground(new Color(241, 245, 244));
    }

    private void configuracionPanel() {
        setLayout(new BorderLayout(0, 10));
        listadoPedidos();
        add(panelBotones(), BorderLayout.SOUTH);
    }

    private void listadoPedidos () {
        String[] columnas = {"ID", "Dirección", "Tipo", "Distancia", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPedidos = new JTable(modeloTabla);

        add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);
        cargarPedidos();
    }

    private JPanel panelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnActualizar = new JButton("Actualizar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnSalir = new JButton("Salir");

        btnActualizar.addActionListener(e -> cargarPedidos());
        btnModificar.addActionListener(e -> editarPedidoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarPedidoSeleccionado());
        btnSalir.addActionListener(e -> dispose());

        panel.add(btnActualizar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnSalir);

        return panel;
    }

    private void editarPedidoSeleccionado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para modificar.",
                    "Seleccione un pedido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idPedido = (int) modeloTabla.getValueAt(fila, 0);
        String direccion = modeloTabla.getValueAt(fila, 1).toString();
        String tipo = modeloTabla.getValueAt(fila, 2).toString();
        int distancia = (int) modeloTabla.getValueAt(fila, 3);
        EstadoPedido estado = (EstadoPedido) modeloTabla.getValueAt(fila, 4);

        JTextField campoDireccion = new JTextField();
        JTextField campoDistancia = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 4, 4));
        panel.add(new JLabel("Dirección:"));
        panel.add(campoDireccion);
        panel.add(new JLabel("Distancia (km):"));
        panel.add(campoDistancia);
        campoDireccion.setText(direccion);
        campoDistancia.setText(String.valueOf(distancia));

        int opcion = JOptionPane.showConfirmDialog(this, panel, "¿Modificar Pedido?", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        String nuevaDireccion = campoDireccion.getText().trim();
        String distanciaTexto = campoDistancia.getText().trim();

        if (nuevaDireccion.isEmpty() || distanciaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete la dirección y la distancia.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int nuevaDistancia = Integer.parseInt(distanciaTexto);
            if (nuevaDistancia <= 0) {
                JOptionPane.showMessageDialog(this, "La distancia debe ser mayor que cero.");
                return;
            }
            PedidoResumen actualizado = new PedidoResumen(idPedido, nuevaDireccion, tipo, nuevaDistancia, estado);
            boolean actualizacionExitosa = controlador.actualizarPedido(actualizado);
            if (actualizacionExitosa) {
                JOptionPane.showMessageDialog(this, "Pedido modificado correctamente.");
                cargarPedidos();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible modificar el pedido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "No se pudo convertir la distancia.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void eliminarPedidoSeleccionado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para eliminar.",
                    "Seleccione un pedido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idPedido = (int) modeloTabla.getValueAt(fila, 0);
        int opcion = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea eliminar el pedido?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        if (controlador.eliminarPedido(idPedido)) {
            JOptionPane.showMessageDialog(this, "Pedido eliminado.");
            cargarPedidos();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible eliminar el pedido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPedidos() {
        modeloTabla.setRowCount(0);
    for (PedidoResumen pedido : pedidoDAOImpl.readAll()){
        Object[] fila = {
                pedido.getIdPedido(),
                pedido.getDireccion(),
                pedido.getTipo(),
                pedido.getDistanciaKm(),
                pedido.getEstado() };
        modeloTabla.addRow(fila);
    }
    }
}
