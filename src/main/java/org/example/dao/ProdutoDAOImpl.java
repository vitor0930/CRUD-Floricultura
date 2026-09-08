package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.config.ConnectionFactory;
import org.example.model.Produto;
import org.example.model.Categoria;

public class ProdutoDAOImpl implements DAO<Produto> {

    @Override
    public boolean salvar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, categoria_id) VALUES (?, ?, ?);";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getCategoria().getId());
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletar(Produto produto) {
        String sql = "DELETE FROM produtos WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produto.getId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, categoria_id = ? WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getCategoria().getId());
            stmt.setInt(4, produto.getId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int categoriaId = rs.getInt("categoria_id");
                Categoria categoria = new CategoriaDAOImpl().buscarPorId(categoriaId);
                return new Produto(id, nome, preco, categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int categoriaId = rs.getInt("categoria_id");
                Categoria categoria = new CategoriaDAOImpl().buscarPorId(categoriaId);
                produtos.add(new Produto(id, nome, preco, categoria));
            }
            return produtos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}