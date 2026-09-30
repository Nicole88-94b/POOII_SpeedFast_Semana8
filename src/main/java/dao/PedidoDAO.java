package dao;

import modelo.PedidoResumen;

import java.util.List;

public interface PedidoDAO {
    void create();
    List<PedidoResumen> readAll();
    void update();
    void delete();
}
