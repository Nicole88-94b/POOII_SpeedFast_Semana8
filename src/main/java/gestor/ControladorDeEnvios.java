package gestor;

import dao.PedidoDAO;
import dao.impl.PedidoDAOImpl;
import interfaces.Cancelable;
import interfaces.Despachable;
import interfaces.Rastreable;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import  java.util.ArrayList;
import  java.util.List;
import  java.util.concurrent.atomic.AtomicInteger;

/**
 * Conserva las operaciones de registro, reserva y seguimiento de pedidos.
 * La simulación de Semana 5 usa {@link ZonaDeCarga} para coordinar las entregas.
 */
public class ControladorDeEnvios implements Rastreable {

    private List<Pedido> pedidos;
    private List<String> historial;
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final AtomicInteger siguienteId = new AtomicInteger(100);


    /**
     * Inicializa las colecciones de pedidos e historial de la ejecución actual.
     */
    public ControladorDeEnvios() {
        this.pedidos = new ArrayList<>();
        this.historial = new ArrayList<>();

    }

    /**
     * Entrega el identificador que se asignará al próximo pedido.
     *
     * @return siguiente identificador disponible
     */
    public int obtenerSiguienteIdPedido() {
        return siguienteId.get();
    }

    /**
     * Reserva y devuelve un nuevo identificador para un pedido.
     *
     * @return identificador único dentro de la ejecución actual
     */
    public int generarIdPedido() {
        return siguienteId.getAndIncrement();
    }

    /**
     * Registra un pedido si no existe otro con el mismo identificador.
     *
     * @param pedido pedido que se desea registrar
     * @return {@code true} si se agregó al registro
     */
    public boolean registrarPedido(Pedido pedido) {
        if (pedido == null) {
            return false;
        }

        for (Pedido pedidoRegistrado : pedidos) {
            if (pedidoRegistrado.getIdPedido() == (pedido.getIdPedido())) {
                return false;
            }
        }

        pedidos.add(pedido);
        historial.add("Pedido " + pedido.getIdPedido() + " registrado.");

        return true;
    }

    /**
     * Solicita la reserva de un pedido previamente registrado.
     *
     * @param pedido pedido que se desea reservar
     * @return {@code true} si la reserva se realizó
     */
    public boolean reservarPedido(Pedido pedido) {
        if (pedido == null || !pedidos.contains(pedido)) {
            return false;
        }
        boolean reservado = pedido.reservar();
        if (reservado) {
            historial.add("Pedido " + pedido.getIdPedido() + " reservado.");
        }
        return reservado;
    }

    /**
     * Despacha un pedido registrado que permite esta operación.
     *
     * @param pedido pedido que se desea despachar
     * @return {@code true} si el despacho se realizó
     */
    public boolean despacharPedido(Pedido pedido) {
        if (pedido == null || !pedidos.contains(pedido) || !(pedido instanceof Despachable)) {
            return false;
        }

        Despachable despachable = (Despachable) pedido;
        boolean despachado = despachable.despachar();

        if (despachado) {
            historial.add("Pedido " + pedido.getIdPedido() + " despachado.");
        }

        return despachado;
    }

    /**
     * Cancela un pedido registrado cuando su estado lo permite.
     *
     * @param pedido pedido que se desea cancelar
     * @return {@code true} si la cancelación se realizó
     */
    public boolean cancelarPedido(Pedido pedido) {
        if (pedido == null || !pedidos.contains(pedido) || !(pedido instanceof Cancelable)) {
            return false;
        }

        Cancelable cancelable = (Cancelable) pedido;
        boolean cancelado = cancelable.cancelar();

        if (cancelado) {
            historial.add("Pedido " + pedido.getIdPedido() + " cancelado.");
        }

        return cancelado;
    }

    public void mostrarPedidosReservados() {
        System.out.println("=========== PEDIDOS RESERVADOS ===========");

        for (Pedido pedido : pedidos) {
            if (pedido.isReservado() && !pedido.isDespachado() && !pedido.isCancelado()) {
                System.out.println(pedido.getIdPedido());
            }
        }
    }

    public void mostrarPedidosDespachados() {
        System.out.println("=========== PEDIDOS DESPACHADOS ===========");
        for (Pedido pedido : pedidos) {
            if (pedido.isDespachado()) {
                System.out.println(pedido.getIdPedido());
            }
        }
    }

    public void mostrarPedidosCancelados() {
        System.out.println("=========== PEDIDOS CANCELADOS ===========");
        for (Pedido pedido : pedidos) {
            if (pedido.isCancelado()) {
                System.out.println(pedido.getIdPedido());
            }
        }
    }


    /**
     * Devuelve una copia de los eventos registrados para proteger el historial interno.
     *
     * @return lista de mensajes del historial
     */
    @Override
    public List<String> verHistorial() {
        return new ArrayList<>(historial);
    }

    /**
     * Entrega una copia de los pedidos para impedir modificaciones directas
     * sobre la colección administrada por el controlador.
     *
     * @return copia de los pedidos registrados
     */
    public List<Pedido> obtenerPedidos() {
        return new ArrayList<>(pedidos);
    }

    private Pedido buscarPedidoPorId(int idPedido) {
        for (Pedido pedido : pedidos) {
            if (pedido.getIdPedido() == idPedido) {
                return pedido;
            }
        }

        return null;
    }

    /**
     * Intenta asignar un repartidor a un pedido pendiente y todavía sin asignación.
     * La clase concreta del pedido comprueba sus propias reglas de compatibilidad.
     *
     * @param idPedido identificador del pedido que recibirá la asignación
     * @param repartidor repartidor seleccionado para realizar la entrega
     * @return {@code true} si la asignación fue aceptada
     */
    public boolean asignarRepartidor(int idPedido, Repartidor repartidor) {
        Pedido pedido = buscarPedidoPorId(idPedido);

        if (pedido == null ||
                repartidor == null ||
                pedido.getEstado() != EstadoPedido.PENDIENTE ||
                pedido.getRepartidor() != null) {
            return false;
        }

        boolean asignado = pedido.asignarRepartidor(repartidor);

        if (!asignado) {
            return false;
        }
        boolean persistido = pedidoDAO.asignarRepartidor(idPedido, repartidor.getIdRepartidor());
        if (persistido) {
            historial.add("Pedido " + pedido.getIdPedido() + " asignado a " + repartidor.getNombreRepartidor());
        }
        return persistido;
    }

    /**
     * Reserva, despacha e ingresa a la zona de carga los pedidos pendientes
     * que tienen un repartidor compatible.
     *
     * @param zonaDeCarga cola compartida donde se dejan los pedidos preparados
     * @return pedidos que quedaron disponibles para la simulación de entrega
     */
    public List<Pedido> prepararPedidosParaEntrega(ZonaDeCarga zonaDeCarga) {
        List<Pedido> pedidosPreparados = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() != EstadoPedido.PENDIENTE || pedido.getRepartidor() == null) {
                continue;
            }

            if (!pedido.cumpleRequisitos()) {
                historial.add("Pedido " + pedido.getIdPedido() + " no cumple los requisitos de entrega.");
                continue;
            }

            boolean reservado = reservarPedido(pedido);
            boolean despachado = reservado && despacharPedido(pedido);

            if (despachado) {
                if (!actualizarEstadoPedido(pedido, EstadoPedido.EN_REPARTO)){
                    historial.add("No fue posible actualizar el pedido " + pedido.getIdPedido() + " a EN_REPARTO.");
                    continue;
                }
                historial.add("Pedido " + pedido.getIdPedido() + " actualizado a EN_REPARTO.");
                zonaDeCarga.agregarPedido(pedido);
                pedidosPreparados.add(pedido);
                historial.add("Pedido " + pedido.getIdPedido() + " ingresado a la zona de carga.");
            }
        }

        return pedidosPreparados;
    }

    public boolean actualizarEstadoPedido(Pedido pedido, EstadoPedido estado) {
        if (pedido == null || estado == null) {
            return false;
        }

        return pedidoDAO.updateEstado(pedido.getIdPedido(), estado);
    }

}
