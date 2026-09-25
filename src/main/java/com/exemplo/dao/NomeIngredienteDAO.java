package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.NomeIngrediente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NomeIngredienteDAO {

    public boolean cadastrarNomeIngrediente(NomeIngrediente nomeIngrediente) throws SQLException {
        String sql = "insert into nome_ingrediente(nome_ingrediente, id_ingrediente) values (?, ?)";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setString(1, nomeIngrediente.getNomeIngrediente());
            pstmt.setInt(2, nomeIngrediente.getIdIngrediente());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<NomeIngrediente> read() throws SQLException {
        String sql = "select * from nome_ingrediente order by id_nome";
        ArrayList<NomeIngrediente> nomeIngrediente = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                NomeIngrediente ni1 = new NomeIngrediente(
                        rset.getString("nome_ingrediente"),
                        rset.getInt("id_ingrediente"),
                        rset.getInt("id_nome")
                );
                nomeIngrediente.add(ni1);
            }
        } return nomeIngrediente;

    }

    public NomeIngrediente readById(int id) throws SQLException {
        String sql = "select * from nome_ingrediente where id_nome =?";
        NomeIngrediente nomeIngrediente = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    nomeIngrediente = new NomeIngrediente(
                            rset.getString("nome_ingrediente"),
                            rset.getInt("id_ingrediente"),
                            rset.getInt("id_nome")
                    );
                }
            }

        } return nomeIngrediente;
    }

    public int alterarValores(NomeIngrediente nomeIngrediente) throws SQLException {
        String sql = "update nome_ingrediente set nome_ingrediente = ?, id_ingrediente = ?, id_ingrediente = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, nomeIngrediente.getNomeIngrediente());
            pstmt.setInt(2, nomeIngrediente.getIdIngrediente());
            pstmt.setInt(2, nomeIngrediente.getIdIngrediente());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from nome_ingrediente where id_nome = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }

    public int deleteByIdIngrediente(int id) throws SQLException {
        String sql = "delete from nome_ingrediente where id_ingrediente = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }
}