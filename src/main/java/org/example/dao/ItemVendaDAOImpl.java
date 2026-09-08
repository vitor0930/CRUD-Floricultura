package org.example.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.example.config.ConnectionFactory;
import org.example.model.ItemVenda;
import org.example.model.Produto;
import org.example.model.Venda;

public class ItemVendaDAOImpl implements DAO<ItemVenda> {

    @Override
    public boolean salvar(ItemVenda itemVenda) {
        String sql = "INSERT INTO itens_vendas (venda_id, produto_id, valor_unitario, quantidade) VALUES (?, ?, ?, ?);";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemVenda.getVenda().getId());
            stmt.setInt(2, itemVenda.getProduto().getId());
            stmt.setDouble(3, itemVenda.getPrecoUnitario().doubleValue());
            stmt.setInt(4, itemVenda.getQuantidade());
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletar(ItemVenda itemVenda) {
        String sql = "DELETE FROM produtos WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemVenda.getId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean atualizar(ItemVenda itemVenda) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, categoria_id = ? WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemVenda.getVenda().getId());
            stmt.setInt(2, itemVenda.getProduto().getId());
            stmt.setDouble(3, itemVenda.getPrecoUnitario().doubleValue());
            stmt.setInt(4, itemVenda.getQuantidade());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ItemVenda buscarPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int produtoId = rs.getInt("produto_id");
                Produto produto = new ProdutoDAOImpl().buscarPorId(produtoId);
                int vendaId = rs.getInt("venda_id");
                Venda venda = new VendaDAOImpl().buscarPorId(vendaId);
                BigDecimal precoUnitario = BigDecimal.valueOf(rs.getDouble("preco_unitario"));
                int quantidade = rs.getInt("quantidade");
                return new ItemVenda(id, produto, venda, precoUnitario, quantidade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ItemVenda> listarTodos() {
        List<ItemVenda> itensVendas = new ArrayList<>();
        String sql = "SELECT * FROM produtos;";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int produtoId = rs.getInt("produto_id");
                Produto produto = new ProdutoDAOImpl().buscarPorId(produtoId);
                int vendaId = rs.getInt("venda_id");
                Venda venda = new VendaDAOImpl().buscarPorId(vendaId);
                BigDecimal precoUnitario = BigDecimal.valueOf(rs.getDouble("preco_unitario"));
                int quantidade = rs.getInt("quantidade");
                itensVendas.add(new ItemVenda(id, produto, venda, precoUnitario, quantidade));
            }
            return itensVendas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
