package vista;

import dao.impl.EntregaDAOImpl;
import gestor.ControladorDeEnvios;
import modelo.EntregaResumen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaGestionEntregas extends JFrame {
    private final EntregaDAOImpl entregaDAOImpl = new EntregaDAOImpl();
    private DefaultTableModel modeloTabla;
    private JTable tablaEntregas;

    public VentanaGestionEntregas() {
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

    private void listadoPedidos() {
        String[] columnas = {"ID", "Pedido", "Repartidor", "Fecha", "Hora"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEntregas = new JTable(modeloTabla);

        add(new JScrollPane(tablaEntregas), BorderLayout.CENTER);
        cargarEntregas();
    }

    private JPanel panelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnActualizar = new JButton("Actualizar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnSalir = new JButton("Salir");

        btnActualizar.addActionListener(e -> cargarEntregas());
        /*
        btnModificar.addActionListener(e -> editarPedidoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarPedidoSeleccionado());

         */
        btnSalir.addActionListener(e -> dispose());

        panel.add(btnActualizar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnSalir);

        return panel;
    }

    public void cargarEntregas() {
        modeloTabla.setRowCount(0);

        for (EntregaResumen entrega : entregaDAOImpl.readAll()) {
            Object[] fila = {entrega.getIdEntrega(),
                    entrega.getIdRepartidor(),
                    entrega.getIdPedido(),
                    entrega.getFecha(),
                    entrega.getHora()
            };
            modeloTabla.addRow(fila);
        }
    }

}
