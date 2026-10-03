package dao.interfaces;

import modelo.Entrega;
import modelo.EntregaResumen;

import java.util.List;

public interface EntregaDAO {
    boolean create(Entrega entrega);
    List<EntregaResumen> readAll();
    boolean update(EntregaResumen entrega);
    boolean delete(int idEntrega);
}
