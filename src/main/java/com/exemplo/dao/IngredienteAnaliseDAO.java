package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.IngredienteAnalise;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IngredienteAnaliseDAO {

    public boolean cadastrarIngredienteAnalise(IngredienteAnalise ingredienteAnalise) throws SQLException {
        String sql = "insert into ingrediente_analise(id_ingrediente) values (?)";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, ingredienteAnalise.getIdIngrediente());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<IngredienteAnalise> read() throws SQLException {
        String sql = "select * from ingrediente_analise order by id_ingrediente_analise";
        ArrayList<IngredienteAnalise> ingredienteAnalise = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                IngredienteAnalise ia1 = new IngredienteAnalise(
                        rset.getInt("id_ingrediente_analise"),
                        rset.getInt("id_ingrediente"),
                        rset.getInt("id_analise")
                );
                ingredienteAnalise.add(ia1);
            }
        } return ingredienteAnalise;

    }

    public IngredienteAnalise readById(int id) throws SQLException {
        String sql = "select * from ingrediente_analise where id_ingrediente_analise =?";
        IngredienteAnalise ingredienteAnalise = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    ingredienteAnalise = new IngredienteAnalise(
                            rset.getInt("id_ingrediente_analise"),
                            rset.getInt("id_ingrediente"),
                            rset.getInt("id_analise")
                    );
                }
            }

        } return ingredienteAnalise;
    }

    public int alterarValores(IngredienteAnalise ingredienteAnalise) throws SQLException {
        String sql = "update ingrediente_analise set id_ingrediente = ?, id_ingrediente = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, ingredienteAnalise.getIdIngrediente());
            pstmt.setInt(1, ingredienteAnalise.getIdIngrediente());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from ingrediente_analise where id_ingrediente_analise = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }

    public int deleteByIdIngrediente(int id) throws SQLException {
        String sql = "delete from ingrediente_analise where id_ingrediente= ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }

    public int deleteByIdAnalise(int id) throws SQLException {
        String sql = "delete from ingrediente_analise where id_analise = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }
}
