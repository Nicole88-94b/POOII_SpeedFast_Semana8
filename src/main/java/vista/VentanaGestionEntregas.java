package vista;

import dao.impl.EntregaDAOImpl;
import dao.impl.PedidoDAOImpl;
import dao.impl.RepartidorDAOImpl;
import modelo.EntregaResumen;
import modelo.EstadoPedido;
import modelo.PedidoResumen;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class VentanaGestionEntregas extends JFrame {
    private final EntregaDAOImpl entregaDAOImpl = new EntregaDAOImpl();
    private DefaultTableModel modeloTabla;
    private JTable tablaEntregas;
    private JTextField campoFecha;
    private JTextField campoHora;
    private final PedidoDAOImpl pedidoDAOImpl = new PedidoDAOImpl();
    private final RepartidorDAOImpl repartidorDAOImpl = new RepartidorDAOImpl();

    public VentanaGestionEntregas() {
        arquitecturaVentana();
    }

    private void arquitecturaVentana() {
        tamanoVentana();
        configuracionPanel();
        setLocationRelativeTo(null);
    }

    private void tamanoVentana() {
        setTitle("Gestión de Entregas");
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
        String[] columnas = {"ID Entrega", "ID Pedido", "ID Repartidor", "Fecha", "Hora"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEntregas = new JTable(modeloTabla);

        add(new JScrollPane(tablaEntregas), BorderLayout.CENTER);
        cargarEntregas();
    }

    private JPanel panelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnSalir = new JButton("Salir");

        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnActualizar.addActionListener(e -> cargarEntregas());
        btnModificar.addActionListener(e -> editarEntregaSeleccionada());
        btnEliminar.addActionListener(e -> eliminarEntregaSeleccionada());


        btnSalir.addActionListener(e -> dispose());

        panel.add(btnRegistrar);
        panel.add(btnActualizar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnSalir);

        return panel;
    }

    private void registrarEntrega() {
        List<PedidoResumen> pedidos = new ArrayList<>();

        for (PedidoResumen pedido : pedidoDAOImpl.readAll()) {
            if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
                pedidos.add(pedido);
            }
        }
        List<Repartidor> repartidores = repartidorDAOImpl.readAll();

        if (pedidos.isEmpty() || repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No existen pedidos entregados o repartidores disponibles.");
            return;
        }

        JComboBox<String> comboPedidos = new JComboBox<>();
        JComboBox<String> comboRepartidores = new JComboBox<>();

        for (PedidoResumen pedido : pedidos) {
            comboPedidos.addItem(pedido.getIdPedido() + " - " + pedido.getTipo());
        }

        for (Repartidor repartidor : repartidores) {
            comboRepartidores.addItem(repartidor.getIdRepartidor() + " - " + repartidor.getNombreRepartidor());
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 4, 4));
        panel.add(new JLabel("Pedido:"));
        panel.add(comboPedidos);
        panel.add(new JLabel("Repartidor:"));
        panel.add(comboRepartidores);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Registrar entrega",
                JOptionPane.OK_CANCEL_OPTION);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        PedidoResumen pedidoSeleccionado = pedidos.get(comboPedidos.getSelectedIndex());
        Repartidor repartidorSeleccionado = repartidores.get(comboRepartidores.getSelectedIndex());

        if (pedidoSeleccionado.getEstado() != EstadoPedido.ENTREGADO) {
            JOptionPane.showMessageDialog(this, "Solo se pueden registrar pedidos entregados.");
            return;
        }
        for (EntregaResumen entregaRegistrada : entregaDAOImpl.readAll()) {
            if (entregaRegistrada.getIdPedido() == pedidoSeleccionado.getIdPedido()) {
                JOptionPane.showMessageDialog(this, "El pedido seleccionado ya tiene una entrega registrada.");
                return;
            }
        }

        EntregaResumen nuevaEntrega = new EntregaResumen(0, pedidoSeleccionado.getIdPedido(),
                repartidorSeleccionado.getIdRepartidor(), LocalDate.now(), LocalTime.now().withNano(0));

        if (entregaDAOImpl.create(nuevaEntrega)) {
            JOptionPane.showMessageDialog(this, "Entrega registrada correctamente.");
            cargarEntregas();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible registrar la entrega.");
        }
    }

    private void eliminarEntregaSeleccionada() {
        int fila = tablaEntregas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una entrega para eliminar.",
                    "Seleccione una entrega", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idEntrega = (int) modeloTabla.getValueAt(fila, 0);
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la entrega?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        if (entregaDAOImpl.delete(idEntrega)) {
            JOptionPane.showMessageDialog(this, "Entrega eliminada.");
            cargarEntregas();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible eliminar la entrega.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarEntregaSeleccionada() {
        int fila = tablaEntregas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una entrega para modificar.",
                    "Seleccione una entrega", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idEntrega = (int) modeloTabla.getValueAt(fila, 0);
        int idPedido = (int) modeloTabla.getValueAt(fila, 1);
        int idRepartidor = (int) modeloTabla.getValueAt(fila, 2);
        String fecha = modeloTabla.getValueAt(fila, 3).toString();
        String hora = modeloTabla.getValueAt(fila, 4).toString();

        JPanel panel = configurarSubPanel(idEntrega, idPedido, idRepartidor, fecha, hora);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "¿Modificar Entrega?", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            LocalDate nuevaFecha = LocalDate.parse(campoFecha.getText().trim());
            LocalTime nuevaHora = LocalTime.parse(campoHora.getText().trim());

            EntregaResumen entregaActualizada = new EntregaResumen(idEntrega, idPedido, idRepartidor, nuevaFecha, nuevaHora);
            if (entregaDAOImpl.update(entregaActualizada)) {
                JOptionPane.showMessageDialog(this, "Entrega modificada correctamente.");
                cargarEntregas();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible modificar la entrega.");
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Use fecha AAAA-MM-DD y hora HH:MM:SS.",
                    "Formato inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel configurarSubPanel(int idEntrega, int idPedido, int idRepartidor, String fecha, String hora) {
        JTextField campoIdEntrega = new JTextField();
        JTextField campoIdPedido = new JTextField();
        JTextField campoIdRepartidor = new JTextField();
        campoFecha = new JTextField();
        campoHora = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 4, 4));
        panel.add(new JLabel("ID Entrega:"));
        panel.add(campoIdEntrega);
        panel.add(new JLabel("ID Pedido:"));
        panel.add(campoIdPedido);
        panel.add(new JLabel("ID Repartidor:"));
        panel.add(campoIdRepartidor);
        panel.add(new JLabel("Fecha:"));
        panel.add(campoFecha);
        panel.add(new JLabel("Hora:"));
        panel.add(campoHora);
        campoIdEntrega.setText(String.valueOf(idEntrega));
        campoIdEntrega.setEditable(false);
        campoIdPedido.setText(String.valueOf(idPedido));
        campoIdPedido.setEditable(false);
        campoIdRepartidor.setText(String.valueOf(idRepartidor));
        campoIdRepartidor.setEditable(false);
        campoFecha.setText(fecha);
        campoHora.setText(hora);
        return panel;
    }

    public void cargarEntregas() {
        modeloTabla.setRowCount(0);

        for (EntregaResumen entrega : entregaDAOImpl.readAll()) {
            Object[] fila = {entrega.getIdEntrega(),
                    entrega.getIdPedido(),
                    entrega.getIdRepartidor(),
                    entrega.getFecha(),
                    entrega.getHora()
            };
            modeloTabla.addRow(fila);
        }
    }

}
