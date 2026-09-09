package org.example.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.config.ConnectionFactory;
import org.example.model.ItemVenda;
import org.example.model.Venda;
import org.example.model.Cliente;

public class VendaDAOImpl implements VendaDAO {

    private final ItemVendaDAOImpl itemVendaDAO = new ItemVendaDAOImpl();
    private final ProdutoDAOImpl produtoDAO = new ProdutoDAOImpl();

    @Override
    public boolean salvar(Venda venda) {
        if (venda == null || venda.getCliente() == null) {
            return false;
        }

        String sql = "INSERT INTO vendas (data_venda, cliente_id, cancelada) VALUES (?, ?, FALSE);";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LocalDateTime agora = LocalDateTime.now();
            stmt.setTimestamp(1, Timestamp.valueOf(agora));
            stmt.setInt(2, venda.getCliente().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venda.setId(rs.getInt(1));
                }
            }
            venda.setData(agora);
            venda.setCancelada(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cancelar(Venda venda) {
        if (venda == null) {
            return false;
        }

        // RN06 e RN07: Atualiza status para cancelada somente se não estiver cancelada ainda
        String sql = "UPDATE vendas SET cancelada = TRUE WHERE id = ? AND cancelada = FALSE;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venda.getId());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // RN07: Uma venda cancelada deve reverter o estoque dos produtos envolvidos
                List<ItemVenda> itens = itemVendaDAO.listarPorVenda(venda.getId());
                for (ItemVenda item : itens) {
                    if (item.getProduto() != null && item.getQuantidade() > 0) {
                        produtoDAO.reporEstoque(item.getProduto().getId(), item.getQuantidade());
                    }
                }
                venda.setCancelada(true);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Venda> listarTodos() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM vendas;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDateTime data = rs.getTimestamp("data_venda").toLocalDateTime();
                int clienteId = rs.getInt("cliente_id");
                boolean cancelada = rs.getBoolean("cancelada");
                Cliente cliente = new ClienteDAOImpl().buscarPorId(clienteId);
                vendas.add(new Venda(id, data, cliente, cancelada));
            }
            return vendas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Venda buscarPorId(int id) {
        String sql = "SELECT * FROM vendas WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LocalDateTime data = rs.getTimestamp("data_venda").toLocalDateTime();
                int clienteId = rs.getInt("cliente_id");
                boolean cancelada = rs.getBoolean("cancelada");
                Cliente cliente = new ClienteDAOImpl().buscarPorId(clienteId);
                return new Venda(id, data, cliente, cancelada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
