package vista;

import dao.impl.PedidoDAOImpl;
import gestor.ControladorDeEnvios;
import modelo.*;

import  javax.swing.*;
import  java.awt.*;

/**
 * Formulario utilizado para validar y guardar nuevos pedidos en MySQL.
 * Los campos específicos de encomienda se habilitan únicamente cuando
 * corresponde a ese tipo de pedido.
 */
public class VentanaRegistroPedido extends JFrame {
    private final ControladorDeEnvios controlador;
    private JTextField campoIdAutomatico;
    private JTextField campoPesoEncomienda;
    private JComboBox<String> campoEstadoEmbalaje;
    private JTextField campoDireccionEntrega;
    private JTextField campoDistanciaKilometros;
    private JComboBox<String> campoTipoPedido;
    private final PedidoDAOImpl pedidoDAOImpl = new PedidoDAOImpl();


    /**
     * Crea el formulario y lo conecta con el controlador de la sesión actual.
     *
     * @param controlador controlador que conservará los pedidos para su simulación
     */
    public VentanaRegistroPedido(ControladorDeEnvios controlador) {
        this.controlador = controlador;
        arquitecturaVentana();
    }

    private void arquitecturaVentana() {
        tamanoVentana();
        panelCamposTexto();
        setLocationRelativeTo(null);
    }

    private void tamanoVentana() {
        setTitle("Registro Pedido");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 420);
        setLocation(500, 300);
        getContentPane().setBackground(new Color(241, 245, 244));
    }

    private void panelCamposTexto() {
        setLayout(new BorderLayout(0, 10));
        add(campos(), BorderLayout.CENTER);
        add(panelBotones(), BorderLayout.SOUTH);
    }

    private JPanel campos() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(241, 245, 244));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        campoDireccionEntrega = new JTextField(10);
        campoDistanciaKilometros = new JTextField(10);
        String[] tipos = {"", "Comida", "Express", "Encomienda"};
        campoTipoPedido = new JComboBox<String>(tipos);
        campoIdAutomatico = new JTextField(10);
        campoIdAutomatico.setEditable(false);
        campoIdAutomatico.setText("Automático");
        campoPesoEncomienda = new JTextField(10);
        String[] opciones = {"", "ACEPTADA", "RECHAZADA"};
        campoEstadoEmbalaje = new JComboBox<String>(opciones);
        campoTipoPedido.addActionListener(e -> actualizarCamposEncomienda());
        actualizarCamposEncomienda();

        distribucionCamposTexto(panel);
        return panel;
    }

    private void distribucionCamposTexto(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(new JLabel("Ingrese los datos del pedido"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campoIdAutomatico, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Dirección de entrega:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campoDireccionEntrega, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Tipo de pedido:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campoTipoPedido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Distancia:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campoDistanciaKilometros, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Peso de la encomienda:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campoPesoEncomienda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Estado del embalaje"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campoEstadoEmbalaje, gbc);


    }

    private void registroDelPedido() {
        String direccion = campoDireccionEntrega.getText().trim();
        String distanciaTexto = campoDistanciaKilometros.getText().trim();
        String tipoPedido = (String) campoTipoPedido.getSelectedItem();

        if (!validarCamposComunes(direccion, distanciaTexto, tipoPedido)) {
            JOptionPane.showMessageDialog(this,
                    "Complete la dirección, el tipo de pedido y la distancia.", "Campos incompletos",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int distanciaPedido = validarDistanciaKilometros(distanciaTexto);
            double pesoEncomienda = 0;
            String estadoEmbalaje = "";

            if ("Encomienda".equals(tipoPedido)) {
                String pesoTexto = campoPesoEncomienda.getText().trim();
                estadoEmbalaje = (String) campoEstadoEmbalaje.getSelectedItem();
                validarCamposEspecificosEncomienda(pesoTexto, estadoEmbalaje);
                pesoEncomienda = validarPeso(pesoTexto);
            }

            int idPedido = 0;
            Pedido pedido;

            switch (tipoPedido) {
                case "Comida":
                    pedido = new PedidoComida(idPedido, direccion, tipoPedido, distanciaPedido);
                    break;

                case "Encomienda":
                    pedido = new PedidoEncomienda(idPedido, direccion, tipoPedido, distanciaPedido,
                            estadoEmbalaje, pesoEncomienda);
                    break;

                case "Express":
                    pedido = new PedidoExpress(idPedido, direccion, tipoPedido, distanciaPedido);
                    break;

                default:
                    throw new IllegalArgumentException("Seleccione un tipo de pedido válido.");
            }
            if (!pedidoDAOImpl.create(pedido)) {
                JOptionPane.showMessageDialog(this, "No fue posible guardar el pedido en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!controlador.registrarPedido(pedido)) {
                JOptionPane.showMessageDialog(this, "No fue posible registrar el pedido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (pedido.getEstado() == EstadoPedido.RECHAZADO) {
                JOptionPane.showMessageDialog(this,
                        "Encomienda " + pedido.getIdPedido() + " registrada como RECHAZADA.\n" +
                                "No se puede iniciar el proceso de entrega.", "Encomienda rechazada", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Pedido " + pedido.getIdPedido() + " registrado correctamente.");
            }
            limpiarCampos();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validarCamposEspecificosEncomienda(String peso, String estado) {
        if (peso.isEmpty()) {
            throw new IllegalArgumentException("Debe registrar el peso de la encomienda.");
        }
        if (estado == null || estado.isEmpty()) {
            throw new IllegalArgumentException("Seleccione el estado del embalaje.");
        }
    }

    private void limpiarCampos() {
        campoTipoPedido.setSelectedIndex(0);
        campoPesoEncomienda.setText("");
        campoDireccionEntrega.setText("");
        campoDistanciaKilometros.setText("");
        campoEstadoEmbalaje.setSelectedIndex(0);
        campoIdAutomatico.setText("Automático");
    }

    private boolean validarCamposComunes(String direccion, String distanciaKilometros, String tipoPedido) {
        return !direccion.isEmpty() && !distanciaKilometros.isEmpty() && tipoPedido != null && !tipoPedido.isEmpty();
    }

    private int validarDistanciaKilometros(String distancia) {
        try {
            int kilometros = Integer.parseInt(distancia);
            if (kilometros <= 0) {
                throw new IllegalArgumentException("La distancia debe ser mayor que cero.");
            }
            return kilometros;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La distancia debe ser un número entero válido.");
        }
    }

    private double validarPeso(String peso) {
        try {
            double pesoEncomienda = Double.parseDouble(peso);
            if (pesoEncomienda <= 0 || pesoEncomienda > 50) {
                throw new IllegalArgumentException("El peso debe ser mayor que 0 y menor o igual a 50 kg.");
            }
            return pesoEncomienda;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El peso debe ser un número válido.");
        }
    }

    private void actualizarCamposEncomienda() {
        boolean esEncomienda = "Encomienda".equals(campoTipoPedido.getSelectedItem());
        campoPesoEncomienda.setEnabled(esEncomienda);
        campoEstadoEmbalaje.setEnabled(esEncomienda);

        if (!esEncomienda) {
            campoPesoEncomienda.setText("");
            campoEstadoEmbalaje.setSelectedIndex(0);
        }
    }

    private JPanel panelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> registroDelPedido());
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        return panel;
    }

}
