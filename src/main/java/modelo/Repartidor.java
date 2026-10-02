package modelo;

import gestor.ZonaDeCarga;

import  java.util.concurrent.ThreadLocalRandom;

/**
 * Representa a un repartidor que procesa sus pedidos desde la zona de carga compartida.
 * Cada instancia se ejecuta en un hilo independiente.
 */
public class Repartidor implements Runnable {
    private String nombreRepartidor;
    private boolean tieneMochilaTermica;
    private boolean disponible;
    private ZonaDeCarga zonaDeCarga;
    private int idRepartidor;

    /**
     * Crea un repartidor y lo vincula con la zona de carga de la simulación.
     *
     * @param nombreRepartidor nombre que identifica al repartidor
     * @param tieneMochilaTermica indica si puede transportar pedidos de comida
     * @param disponible indica si puede aceptar pedidos
     * @param zonaDeCarga zona compartida desde la que retirará pedidos
     * @throws IllegalArgumentException si el nombre está vacío
     */
    public Repartidor(String nombreRepartidor, boolean tieneMochilaTermica, boolean disponible,
                      ZonaDeCarga zonaDeCarga) {
        setNombreRepartidor(nombreRepartidor);
        setTieneMochilaTermica(tieneMochilaTermica);
        setDisponible(disponible);
        this.zonaDeCarga = zonaDeCarga;
    }

    public Repartidor (int idRepartidor, String nombreRepartidor, boolean tieneMochilaTermica, boolean disponible) {
        this.idRepartidor = idRepartidor;
        setNombreRepartidor(nombreRepartidor);
        setTieneMochilaTermica(tieneMochilaTermica);
        setDisponible(disponible);
    }

    public Repartidor (String nombreRepartidor) {
        setNombreRepartidor(nombreRepartidor);
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    /**
     * Cambia el nombre del repartidor después de validar que tenga contenido.
     *
     * @param nombreRepartidor nuevo nombre
     * @throws IllegalArgumentException si el nombre es nulo o está vacío
     */
    public void setNombreRepartidor(String nombreRepartidor) throws IllegalArgumentException {
        if (nombreRepartidor == null || nombreRepartidor.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del repartidor es obligatorio.");
        }
        this.nombreRepartidor = nombreRepartidor;
    }

    public boolean isTieneMochilaTermica() {
        return tieneMochilaTermica;
    }

    public void setTieneMochilaTermica(boolean tieneMochilaTermica) {
        this.tieneMochilaTermica = tieneMochilaTermica;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        String estadoDisponibilidad = disponible ? "Está disponible el repartidor" : "El repartidor está ocupado";
        String estadoMochila = tieneMochilaTermica ? "Correcto." : "Incorrecto: no tiene mochila térmica.";

        return "Nombre del repartidor: " + nombreRepartidor +
                "\nDisponibilidad: " + estadoDisponibilidad + "."
                + "\nPosee mochila térmica: " + estadoMochila;
    }


    /**
     * Retira y entrega secuencialmente los pedidos asignados a este repartidor.
     * Cada entrega pasa por EN_REPARTO y ENTREGADO; una interrupción detiene el hilo.
     */
    @Override
    public void run() {
        Pedido pedido = zonaDeCarga.retirarPedido(this);
        int entregasRealizadas = 0;
        while (pedido != null) {
            EstadoPedido estadoAnterior = pedido.getEstado();
            pedido.setEstado(EstadoPedido.EN_REPARTO);
            System.out.println("[Pedido " + pedido.getIdPedido() + "] " +
                            estadoAnterior + " -> " + pedido.getEstado());

            int tiempoEspera = ThreadLocalRandom.current().nextInt(1000, 3001);

            try {
                Thread.sleep(tiempoEspera);

                estadoAnterior = pedido.getEstado();
                pedido.setEstado(EstadoPedido.ENTREGADO);
                entregasRealizadas++;

                System.out.println("[Pedido " + pedido.getIdPedido() + "] " +
                                estadoAnterior + " -> " + pedido.getEstado());
                System.out.println("\n==================== RESUMEN FINAL ====================");
                System.out.println(pedido.mostrarResumen());
                System.out.println("Repartidor asignado: " + nombreRepartidor);
                System.out.println("==============================================\n");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                System.out.println("[Repartidor: " + nombreRepartidor + "] Entrega interrumpida."
                );

                return;
            }
            pedido = zonaDeCarga.retirarPedido(this);
        }

        if (entregasRealizadas == 0) {
            System.out.println("[Repartidor: " + nombreRepartidor +
                    "] no tuvo pedidos aceptados para entregar.");
        } else {
            System.out.println("[Repartidor: " + nombreRepartidor +
                    "] ha entregado " + entregasRealizadas
                    + " pedido(s) con éxito.");
        }



    }
}
