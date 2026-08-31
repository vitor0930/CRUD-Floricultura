package DAO;

import java.util.List;

public interface DAO<T> {
    boolean salvar(T t);
    boolean deletar(T t);
    boolean atualizar(T t);
    List<T> listarTodos();
    T buscarPorId(int id);
}
