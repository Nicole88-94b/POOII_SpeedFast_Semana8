package dao;

import modelo.Entrega;

import java.util.List;

public interface EntregaDAO {
    void create();
    List<Entrega> readAll();
    void update();
    void delete();
}
