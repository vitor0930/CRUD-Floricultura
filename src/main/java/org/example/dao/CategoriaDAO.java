package org.example.dao;

import java.util.List;

import org.example.model.Categoria;

public interface CategoriaDAO {
    boolean inserir(Categoria categoria);
    boolean excluir(Categoria categoria);
    boolean atualizar(Categoria categoria);
    Categoria buscarPorId(int id);
    List<Categoria> listarTodos();
}