package dao;


import modelo.Repartidor;

import java.util.List;

public interface RepartidorDAO {
    void create();
    List<Repartidor> readAll();
    void update();
    void delete();
}
