package dao;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoResumen;

import java.util.List;

public interface PedidoDAO {
    boolean create(Pedido pedido);
    List<PedidoResumen> readAll();
    boolean update(Pedido pedido);
    boolean delete(int idPedido);
    boolean asignarRepartidor(int idPedido, int idRepartidor);
    boolean updateEstado(int idPedido, EstadoPedido estado);
}
