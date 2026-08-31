package DAO;

import Model.Venda;

import java.util.List;

public interface VendaDAO {
    boolean salvar(Venda venda);
    boolean cancelar(Venda venda);
    List<Venda> listarTodos();
    Venda buscarPorId(int id);
}
