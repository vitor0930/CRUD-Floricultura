package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.example.config.ConnectionFactory;
import org.example.model.Categoria;

public class CategoriaDAOImpl implements CategoriaDAO{

    @Override 
    public boolean inserir(Categoria categoria) {
        String sql = "INSERT INTO categorias (nome) VALUES (?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, categoria.getNome());
                stmt.execute();
                return true;
             } catch (Exception e) {
                e.printStackTrace();
             }
             return false;
            
    }

    @Override 
    public boolean excluir(Categoria categoria) {
        String sql = "DELETE FROM categorias WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, categoria.getId());
                stmt.executeUpdate();
                return true;
             } catch (Exception e) {
                e.printStackTrace();
             }
             return false;
    }

    @Override 
    public boolean atualizar (Categoria categoria) {
        String sql = "UPDATE categorias SET nome = ? WHERE id = ?;";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, categoria.getNome());
                stmt.setInt(2, categoria.getId());
                stmt.executeUpdate();
                return true;
             } catch (Exception e) {
                e.printStackTrace();
             }
             return false;
    }

    @Override 
    public Categoria buscarPorId(int id) {
     String sql = "SELECT * FROM categorias WHERE id = ?;";
     try (Connection conn = ConnectionFactory.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categoria(rs.getInt("id"), rs.getString("nome"));
            }
          }  catch (Exception e) {
            e.printStackTrace();
          }
          return null;
    }
    
    @Override 
    public List<Categoria> listarTodos() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias;";
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    categorias.add(new Categoria(rs.getInt("id"), rs.getString("nome")));
                }
                return categorias;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
}
