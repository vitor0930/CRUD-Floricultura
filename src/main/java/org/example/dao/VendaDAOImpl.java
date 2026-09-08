package org.example.dao;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.config.ConnectionFactory;
import org.example.model.Venda;
import org.example.model.Cliente;

public class VendaDAOImpl implements VendaDAO {

    @Override
    public boolean salvar(Venda venda) {
        String sql = "INSERT INTO vendas (data_venda, cliente_id) VALUES (?, ?);";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, venda.getCliente().getId());
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cancelar(Venda venda) {
        String sql = "UPDATE vendas SET cancelada = TRUE WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venda.getId());
            stmt.executeUpdate();
            return true;
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

