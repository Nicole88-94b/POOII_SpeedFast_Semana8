package vista;

import dao.interfaces.RepartidorDAO;
import dao.impl.RepartidorDAOImpl;
import gestor.ZonaDeCarga;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Consulta MySQL y presenta los pedidos persistidos en una tabla actualizable.
 */
public class VentanaGestionRepartidores extends JFrame {
    private final RepartidorDAO repartidorDAO = new RepartidorDAOImpl();
    private DefaultTableModel modeloTabla;
    private JTable tablaRepartidores;
    private JTextField campoRepartidor;
    private final ZonaDeCarga zonaDeCarga = new ZonaDeCarga();


    /**
     * Crea la ventana y carga los pedidos disponibles en la base de datos.
     */
    public VentanaGestionRepartidores() {
        arquitecturaVentana();
    }

    private void arquitecturaVentana() {
        tamanoVentana();
        configuracionPanel();
        setLocationRelativeTo(null);
    }

    private void tamanoVentana() {
        setTitle("Gestión de Repartidores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 400);
        setLocation(500, 300);
        getContentPane().setBackground(new Color(241, 245, 244));
    }

    private void configuracionPanel() {
        setLayout(new BorderLayout(0, 10));
        listadoRepartidores();
        add(panelRepartidor(), BorderLayout.NORTH);
        add(panelBotones(), BorderLayout.SOUTH);
    }

    private JPanel panelRepartidor() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nombre"), gbc);


        campoRepartidor = new JTextField(10);
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        panel.add(campoRepartidor, gbc);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrarRepartidor());

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(btnRegistrar, gbc);

        return panel;
    }

    private void listadoRepartidores() {
        String[] columnas = {"ID", "Nombre", "Mochila Térmica", "Disponible"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaRepartidores = new JTable(modeloTabla);
        tablaRepartidores.setDefaultEditor(Object.class, null);
        add(new JScrollPane(tablaRepartidores), BorderLayout.CENTER);
        cargarRepartidores();
    }

    private JPanel panelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnModificar = new JButton("Modificar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnSalir = new JButton("Salir");

        btnModificar.addActionListener(e -> modificarRepartidor());
        btnActualizar.addActionListener(e -> cargarRepartidores());
        btnEliminar.addActionListener(e -> eliminarRepartidor());
        btnSalir.addActionListener(e -> dispose());

        panel.add(btnModificar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnSalir);

        return panel;
    }

    private void eliminarRepartidor() {
        int fila = tablaRepartidores.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un repartidor para eliminar.", "Seleccione un repartidor", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //Consigo los valores de la fila seleccionada
        int idRepartidor = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = modeloTabla.getValueAt(fila, 1).toString();

        //Mensaje de confirmación
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el repartidor " +
                nombre + "?", "Eliminar repartidor.", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        //Elimino el repartidor
        if (repartidorDAO.delete(idRepartidor)) {
            JOptionPane.showMessageDialog(this, "Repartidor " + nombre + " eliminado.");
            cargarRepartidores();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible eliminar el repartidor. Puede " +
                    "estar asociado a una entrega", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void modificarRepartidor() {
        int fila = tablaRepartidores.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un repartidor para modificar.", "Seleccione un repartidor", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //Consigo los valores de la fila seleccionada
        int idRepartidor = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = modeloTabla.getValueAt(fila, 1).toString();
        boolean mochila = "Sí".equals(modeloTabla.getValueAt(fila, 2));
        boolean disponible = "Sí".equals(modeloTabla.getValueAt(fila, 3));

        //Creo nnuevos componentes para modificar los datos
        JTextField campoNombre = new JTextField(nombre);
        JCheckBox opcionMochila = new JCheckBox("Tiene mochila térmica", mochila);
        JCheckBox opcionDisponible = new JCheckBox("Está disponible", disponible);

        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(campoNombre);
        panel.add(opcionMochila);
        panel.add(opcionDisponible);

        //Valido los datos
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Modificar repartidor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        String nuevoNombre = campoNombre.getText().trim();
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre válido.", "Dato obligatorio", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Creo y actualizo el repartidor
        Repartidor repartidor = new Repartidor(idRepartidor, nuevoNombre, opcionMochila.isSelected(), opcionDisponible.isSelected());
        if (repartidorDAO.update(repartidor)) {
            JOptionPane.showMessageDialog(this, "Repartidor modificado correctamente.");
            cargarRepartidores();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible modificar el repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void registrarRepartidor() {
        JCheckBox opcionMochila = new JCheckBox("Tiene mochila térmica");
        JCheckBox opcionDisponible = new JCheckBox("Está disponible");

        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(opcionMochila);
        panel.add(opcionDisponible);

        String nombre = campoRepartidor.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre válido.", "Dato obligatorio", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Registrar repartidor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        Repartidor repartidor = new Repartidor(nombre, opcionMochila.isSelected(), opcionDisponible.isSelected(), zonaDeCarga);

        if (repartidorDAO.create(repartidor)) {
            JOptionPane.showMessageDialog(this, "Repartidor " + nombre + " registrado.");
            campoRepartidor.setText("");
            cargarRepartidores();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible create el repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarRepartidores() {
        modeloTabla.setRowCount(0);
        for (Repartidor repartidor : repartidorDAO.readAll()) {
            Object[] fila = {
                    repartidor.getIdRepartidor(),
                    repartidor.getNombreRepartidor(),
                    repartidor.isTieneMochilaTermica() ? "Sí" : "No",
                    repartidor.isDisponible() ? "Sí" : "No"
            };
            modeloTabla.addRow(fila);
        }
    }
}
