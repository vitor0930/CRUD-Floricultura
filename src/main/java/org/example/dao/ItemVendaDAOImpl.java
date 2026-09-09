package org.example.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.example.config.ConnectionFactory;
import org.example.model.ItemVenda;
import org.example.model.Produto;
import org.example.model.Venda;

public class ItemVendaDAOImpl implements DAO<ItemVenda> {

    private final ProdutoDAOImpl produtoDAOImpl = new ProdutoDAOImpl();

    @Override
    public boolean salvar(ItemVenda itemVenda) {
        if (itemVenda == null || itemVenda.getProduto() == null || itemVenda.getQuantidade() <= 0) {
            return false;
        }

        // RN08: Reduzir estoque antes de registrar o item. Falha se não houver estoque suficiente.
        boolean reduziu = produtoDAOImpl.reduzirEstoque(itemVenda.getProduto().getId(), itemVenda.getQuantidade());
        if (!reduziu) {
            System.err.println("Estoque insuficiente para o produto ID: " + itemVenda.getProduto().getId());
            return false;
        }

        String sql = "INSERT INTO itens_vendas (venda_id, produto_id, valor_unitario, quantidade) VALUES (?, ?, ?, ?);";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, itemVenda.getVenda().getId());
            stmt.setInt(2, itemVenda.getProduto().getId());
            stmt.setBigDecimal(3, itemVenda.getPrecoUnitario());
            stmt.setInt(4, itemVenda.getQuantidade());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    itemVenda.setId(rs.getInt(1));
                }
            }
            return true;
        } catch (Exception e) {
            // Em caso de falha na inserção, estorna o estoque para manter a consistência
            produtoDAOImpl.reporEstoque(itemVenda.getProduto().getId(), itemVenda.getQuantidade());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletar(ItemVenda itemVenda) {
        if (itemVenda == null) {
            return false;
        }

        ItemVenda existente = buscarPorId(itemVenda.getId());
        if (existente == null) {
            return false;
        }

        String sql = "DELETE FROM itens_vendas WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemVenda.getId());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Reverte o estoque do item removido
                produtoDAOImpl.reporEstoque(existente.getProduto().getId(), existente.getQuantidade());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean atualizar(ItemVenda itemVenda) {
        if (itemVenda == null) {
            return false;
        }

        ItemVenda existente = buscarPorId(itemVenda.getId());
        if (existente == null) {
            return false;
        }

        int diff = itemVenda.getQuantidade() - existente.getQuantidade();
        if (diff > 0) {
            boolean reduziu = produtoDAOImpl.reduzirEstoque(itemVenda.getProduto().getId(), diff);
            if (!reduziu) {
                System.err.println("Estoque insuficiente para atualizar quantidade do item.");
                return false;
            }
        } else if (diff < 0) {
            produtoDAOImpl.reporEstoque(itemVenda.getProduto().getId(), -diff);
        }

        String sql = "UPDATE itens_vendas SET venda_id = ?, produto_id = ?, valor_unitario = ?, quantidade = ? WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemVenda.getVenda().getId());
            stmt.setInt(2, itemVenda.getProduto().getId());
            stmt.setBigDecimal(3, itemVenda.getPrecoUnitario());
            stmt.setInt(4, itemVenda.getQuantidade());
            stmt.setInt(5, itemVenda.getId());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return true;
            } else {
                // Se a atualização falhou, estorna a alteração do estoque
                if (diff > 0) {
                    produtoDAOImpl.reporEstoque(itemVenda.getProduto().getId(), diff);
                } else if (diff < 0) {
                    produtoDAOImpl.reduzirEstoque(itemVenda.getProduto().getId(), -diff);
                }
            }
        } catch (Exception e) {
            if (diff > 0) {
                produtoDAOImpl.reporEstoque(itemVenda.getProduto().getId(), diff);
            } else if (diff < 0) {
                produtoDAOImpl.reduzirEstoque(itemVenda.getProduto().getId(), -diff);
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ItemVenda buscarPorId(int id) {
        String sql = "SELECT * FROM itens_vendas WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int produtoId = rs.getInt("produto_id");
                Produto produto = produtoDAOImpl.buscarPorId(produtoId);
                int vendaId = rs.getInt("venda_id");
                Venda venda = new VendaDAOImpl().buscarPorId(vendaId);
                BigDecimal precoUnitario = rs.getBigDecimal("valor_unitario");
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
        String sql = "SELECT * FROM itens_vendas;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int produtoId = rs.getInt("produto_id");
                Produto produto = produtoDAOImpl.buscarPorId(produtoId);
                int vendaId = rs.getInt("venda_id");
                Venda venda = new VendaDAOImpl().buscarPorId(vendaId);
                BigDecimal precoUnitario = rs.getBigDecimal("valor_unitario");
                int quantidade = rs.getInt("quantidade");
                itensVendas.add(new ItemVenda(id, produto, venda, precoUnitario, quantidade));
            }
            return itensVendas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ItemVenda> listarPorVenda(int vendaId) {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT * FROM itens_vendas WHERE venda_id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int produtoId = rs.getInt("produto_id");
                Produto produto = produtoDAOImpl.buscarPorId(produtoId);
                Venda venda = new VendaDAOImpl().buscarPorId(vendaId);
                BigDecimal precoUnitario = rs.getBigDecimal("valor_unitario");
                int quantidade = rs.getInt("quantidade");
                itens.add(new ItemVenda(id, produto, venda, precoUnitario, quantidade));
            }
            return itens;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itens;
    }
}
