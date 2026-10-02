package vista;

import dao.impl.EntregaDAOImpl;
import dao.impl.RepartidorDAOImpl;
import gestor.ControladorDeEnvios;
import gestor.ZonaDeCarga;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import  javax.swing.*;
import  java.awt.*;
import  java.awt.event.ActionEvent;
import  java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import  java.util.ArrayList;
import  java.util.List;
import  java.util.concurrent.ExecutorService;
import  java.util.concurrent.Executors;
import  java.util.concurrent.TimeUnit;

/**
 * Ventana principal de SpeedFast.
 * Comparte el controlador, la zona de carga y los repartidores entre las
 * operaciones de registro, consulta, asignación, entrega y persistencia.
 */
public class VentanaPrincipal extends JFrame {
    ControladorDeEnvios  controlador = new ControladorDeEnvios();
    private final ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
    private final List<Repartidor> repartidores = new ArrayList<>();
    private JTextArea areaDeTrabajo;
    private final RepartidorDAOImpl repartidorDAOImpl = new RepartidorDAOImpl();
    private final EntregaDAOImpl entregaDAOImpl = new EntregaDAOImpl();

    public JTextArea getAreaDeTrabajo() {
        return areaDeTrabajo;
    }

    /**
     * Crea la ventana principal e inicializa los recursos utilizados durante
     * toda la ejecución de la aplicación.
     */
    public VentanaPrincipal() {
        crearRepartidores();
        estructuraBase();
        panelVentana();
        setLocationRelativeTo(null);
    }

    private void crearRepartidores() {
        repartidores.add(new Repartidor("Valentina Contreras", true, true, zonaDeCarga));
        repartidores.add(new Repartidor("Camilo Henriquez", true, false, zonaDeCarga));
        repartidores.add(new Repartidor("Tomas Liencura", false, true, zonaDeCarga));
        repartidores.add(new Repartidor("Javiera Soto", true, true, zonaDeCarga));
        repartidores.add(new Repartidor("Diego Morales", false, true, zonaDeCarga));
        repartidores.add(new Repartidor("Francisca Rojas", true, false, zonaDeCarga));
    }

    private void estructuraBase() {
        setTitle("REPARTOS A DOMICILIO SPEEDFAST");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocation(500, 300);
        getContentPane().setBackground(new Color(241, 245, 244));
    }

    private void panelVentana() {
        setLayout(new GridBagLayout());
        distribucionImagen();
        distribucionBotones();
        distribucionArea();

    }

    private void distribucionImagen() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.weightx = 0.35;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelLogo(), gbc);
    }

    private void distribucionBotones() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(botones(), gbc);
    }

    private void distribucionArea() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(nuevaArea(), gbc);
    }

    private JPanel botones() {
        JPanel botones = new JPanel();
        botones.setBackground(new Color(231, 237, 235));
        botones.setLayout(new GridBagLayout());
        GridBagConstraints sizeMasterbtn = new GridBagConstraints();
        sizeMasterbtn.insets = new Insets(8,8,8,8);
        sizeMasterbtn.fill = GridBagConstraints.HORIZONTAL;

        JButton btnRegistroPedido = new JButton("Registrar Pedido");
        btnRegistroPedido.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistroPedido.setBackground(new Color(37, 91, 82));
        btnRegistroPedido.setForeground(Color.WHITE);
        btnRegistroPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaRegistroPedido ventanaRegistro =
                        new VentanaRegistroPedido(controlador);

                ventanaRegistro.setVisible(true);
            }
        });

        JButton btnListarPedido = new JButton("Listar Pedidos");
        btnListarPedido.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnListarPedido.setBackground(new Color(37, 91, 82));
        btnListarPedido.setForeground(Color.WHITE);
        btnListarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaListaPedidos ventanaListaPedidos = new VentanaListaPedidos();
                ventanaListaPedidos.setVisible(true);
            }
        });

        JButton btnRegistroRepartidor = new JButton("Gestionar Repartidores");
        btnRegistroRepartidor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistroRepartidor.setBackground(new Color(37, 91, 82));
        btnRegistroRepartidor.setForeground(Color.WHITE);
        btnRegistroRepartidor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaGestionRepartidores ventanaGestionRep = new VentanaGestionRepartidores();
                ventanaGestionRep.setVisible(true);
            }
        });

        JButton btnAsignarRepartidor = new JButton("Asignar Repartidor");
        btnAsignarRepartidor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAsignarRepartidor.setBackground(new Color(37, 91, 82));
        btnAsignarRepartidor.setForeground(Color.WHITE);
        btnAsignarRepartidor.addActionListener(
                e -> mostrarAsignacionRepartidor()
        );

        JButton btnIniciar = new JButton("Iniciar Pedido");
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIniciar.setBackground(new Color(197, 111, 44));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.addActionListener(e -> iniciarEntregas());

        botones.add(btnRegistroPedido);
        botones.add(btnListarPedido,sizeMasterbtn);
        botones.add(btnRegistroRepartidor, sizeMasterbtn);
        botones.add(btnAsignarRepartidor, sizeMasterbtn);
        botones.add(btnIniciar,sizeMasterbtn);

        return  botones;
    }

    private JPanel panelLogo() {
        LogoSpeedFast imagenLogo = new LogoSpeedFast();
        JLabel lblLogo = imagenLogo.crearLabelSpeedFast();

        JPanel imagen = new JPanel(new GridBagLayout());
        imagen.setBackground(new Color(241, 245, 244));
        imagen.add(lblLogo);
        return imagen;
    }

    private JScrollPane nuevaArea() {
        areaDeTrabajo = new JTextArea(8,35);
        areaDeTrabajo.setEditable(false);
        areaDeTrabajo.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaDeTrabajo.setForeground(new Color(35, 48, 52));
        areaDeTrabajo.setBackground(new Color(250, 252, 251));

        JScrollPane scroll = new JScrollPane(areaDeTrabajo);
        scroll.setBackground(new Color(241, 245, 244));
        return scroll;
    }

    private void mostrarAsignacionRepartidor() {
        List<Pedido> pedidosDisponibles = new ArrayList<>();

        for (Pedido pedido : controlador.obtenerPedidos()) {
            if (pedido.getEstado() == EstadoPedido.PENDIENTE && pedido.getRepartidor() == null) {
                pedidosDisponibles.add(pedido);
            }
        }

        if (pedidosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No existen pedidos pendientes sin repartidor.");
            return;
        }

        JComboBox<String> comboPedidos = new JComboBox<>();
        JComboBox<String> comboRepartidores = new JComboBox<>();


        for (Pedido pedido : pedidosDisponibles) {
            comboPedidos.addItem(pedido.getIdPedido() + " - " + pedido.getTipoPedido());
        }

        for (Repartidor repartidor : repartidores) {
            comboRepartidores.addItem(repartidor.getNombreRepartidor());
        }

        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Pedido:"));
        panel.add(comboPedidos);
        panel.add(new JLabel("Repartidor:"));
        panel.add(comboRepartidores);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Asignar repartidor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        Pedido pedidoSeleccionado = pedidosDisponibles.get(comboPedidos.getSelectedIndex());
        Repartidor repartidorSeleccionado = repartidores.get(comboRepartidores.getSelectedIndex());

        if (!asegurarRepartidor(repartidorSeleccionado)) {
            JOptionPane.showMessageDialog(this, "No fue posible guardar el repartidor en la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean asignado = controlador.asignarRepartidor(
                pedidoSeleccionado.getIdPedido(),
                repartidorSeleccionado);

        if (asignado) {
            areaDeTrabajo.append(
                    "Pedido " + pedidoSeleccionado.getIdPedido() + " asignado a " + repartidorSeleccionado.getNombreRepartidor() +
                            ".\n");
        } else {
            JOptionPane.showMessageDialog(
                    this, "No fue posible realizar la asignación.\n" +
                            "Compruebe la disponibilidad, la mochila térmica y el estado del pedido.",
                    "Asignación rechazada", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void iniciarEntregas() {
        List<Pedido> pedidosPreparados = controlador.prepararPedidosParaEntrega(zonaDeCarga);

        if (pedidosPreparados.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "No existen pedidos asignados que puedan iniciar su entrega.",
                    "Sin pedidos disponibles", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        areaDeTrabajo.append("\n====================INICIO DE ENTREGAS ====================\n");
        for (Pedido pedido : pedidosPreparados) {
            areaDeTrabajo.append("Pedido " + pedido.getIdPedido() + " preparado para iniciar su entrega."
                    + "\n");
        }

        ExecutorService ejecutor = Executors.newFixedThreadPool(repartidores.size());

        for (Repartidor repartidor : repartidores) {
            ejecutor.submit(repartidor);
        }

        ejecutor.shutdown();

        Thread supervisor = new Thread(() -> {
                try {
                    boolean finalizado =
                            ejecutor.awaitTermination(1, TimeUnit.MINUTES);

                    if (!finalizado) {
                        SwingUtilities.invokeLater(() ->
                                areaDeTrabajo.append("La simulación no terminó dentro del tiempo esperado.\n"));
                        return;
                    }

                    List<String> resultadosPersistencia = new ArrayList<>();

                    for (Pedido pedido : pedidosPreparados) {
                        if (pedido.getEstado() != EstadoPedido.ENTREGADO) {
                            continue;
                        }

                        Entrega entrega = new Entrega(pedido, pedido.getRepartidor(), LocalDate.now(), LocalTime.now());

                        if (entregaDAOImpl.guardar(entrega)) {
                            resultadosPersistencia.add("Entrega " + entrega.getIdEntrega() + " guardada para el pedido "
                                    + pedido.getIdPedido() + ".");
                        } else {
                            resultadosPersistencia.add("No fue posible guardar la entrega del pedido " + pedido.getIdPedido() + ".");
                        }
                    }

                    SwingUtilities.invokeLater(() -> {
                        areaDeTrabajo.append(
                                "==================== RESULTADO DE ENTREGAS ====================\n"
                        );

                        for (Pedido pedido : pedidosPreparados) {
                            areaDeTrabajo.append(pedido.mostrarResumen()
                                            + "\nRepartidor asignado: "
                                            + pedido.getRepartidor().getNombreRepartidor()
                                            + "\n-----------------------------------------------------\n");
                        }

                        for (String resultado : resultadosPersistencia) {
                            areaDeTrabajo.append(resultado + "\n");
                        }
                    });

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                    SwingUtilities.invokeLater(() ->
                            areaDeTrabajo.append(
                                    "La simulación fue interrumpida.\n"
                            )
                    );
                }
            });
        supervisor.start();
    }


    /**
     * Guarda los repartidores iniciales la primera vez que son seleccionados.
     *
     * @param repartidor repartidor que participará en una asignación
     * @return {@code true} si ya posee un identificador persistido
     */
    private boolean asegurarRepartidor(Repartidor repartidor) {
        if (repartidor.getIdRepartidor() > 0) {
            return true;
        }
        return repartidorDAOImpl.create(repartidor);
    }

}
