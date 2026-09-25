package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Ingredientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IngredientesDAO {

    public boolean cadastrarIngredientes(Ingredientes ingredientes) throws SQLException {
        String sql = "insert into ingredientes(nivel_perigo, tipo, dt_atualizacao) values (?, ?, ?)";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, ingredientes.getNivelPerigo());
            pstmt.setString(2, ingredientes.getTipo());
            pstmt.setString(3, ingredientes.getDtAtualizacao());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<Ingredientes> read() throws SQLException {
        String sql = "select * from ingredientes order by id_ingrediente";
        ArrayList<Ingredientes> ingredientes = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                Ingredientes ing1 = new Ingredientes(
                        rset.getInt("id_ingrediente"),
                        rset.getInt("nivel_perigo"),
                        rset.getString("tipo"),
                        rset.getString("dt_atualizacao")
                );
                ingredientes.add(ing1);
            }
        } return ingredientes;

    }

    public Ingredientes readById(int id) throws SQLException {
        String sql = "select * from ingredientes where id_ingrediente =?";
        Ingredientes ingredientes = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    ingredientes = new Ingredientes(
                            rset.getInt("id_ingrediente"),
                            rset.getInt("nivel_perigo"),
                            rset.getString("tipo"),
                            rset.getString("dt_atualizacao")
                    );
                }
            }

        } return ingredientes;
    }

    public int alterarValores(Ingredientes ingredientes) throws SQLException {
        String sql = "update ingredientes set nivel_perigo = ?, tipo = ?, dt_atualizacao = ? where id_ingrediente = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, ingredientes.getNivelPerigo());
            pstmt.setString(2, ingredientes.getTipo());
            pstmt.setString(3, ingredientes.getDtAtualizacao());
            pstmt.setInt(4, ingredientes.getIdIngrediente());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from ingredientes where id_ingrediente = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }
}