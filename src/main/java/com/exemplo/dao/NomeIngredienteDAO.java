package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.NomeIngrediente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela nome_ingrediente: guarda os nomes de cada ingrediente.
public class NomeIngredienteDAO {

    // Cadastra um novo nome para um ingrediente.
    public boolean cadastrar(NomeIngrediente nomeIngrediente) throws SQLException {
        String sql = "insert into nome_ingrediente(nome_ingrediente, id_ingrediente) values (?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setString(1, nomeIngrediente.getNomeIngrediente());
            pstmt.setInt(2, nomeIngrediente.getIdIngrediente());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Lista todos os nomes de ingredientes, em ordem de id.
    public ArrayList<NomeIngrediente> listar() throws SQLException {
        String sql = "select * from nome_ingrediente order by id_nome";
        ArrayList<NomeIngrediente> nomeIngrediente = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                NomeIngrediente nI1 = new NomeIngrediente(
                        rset.getString("nome_ingrediente"),
                        rset.getInt("id_ingrediente"),
                        rset.getInt("id_nome")
                );
                nomeIngrediente.add(nI1);
            }
        } return nomeIngrediente;

    }

    // Busca um nome pelo id. Usado para preencher o formulário de alteração.
    public NomeIngrediente listarPorId(int id) throws SQLException {
        String sql = "select * from nome_ingrediente where id_nome =?";
        NomeIngrediente nomeIngrediente = null;

        try (Connection cnn = ConexaoBanco.conectar();
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

    // Lista todos os nomes do ingrediente escolhido.
    public ArrayList<NomeIngrediente> listarPorIdIngrediente(int id) throws SQLException {
        String sql = "select * from nome_ingrediente where id_ingrediente =?";
        ArrayList<NomeIngrediente> listaNomeIngredientes = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    NomeIngrediente nomeIngrediente = new NomeIngrediente(
                            rset.getString("nome_ingrediente"),
                            rset.getInt("id_ingrediente"),
                            rset.getInt("id_nome")
                    );
                    listaNomeIngredientes.add(nomeIngrediente);
                }
            }

        } return listaNomeIngredientes;
    }


    // Altera o texto do nome escolhido.
    public int alterar(NomeIngrediente nomeIngrediente) throws SQLException {
        String sql = "update nome_ingrediente set nome_ingrediente = ? where id_nome = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, nomeIngrediente.getNomeIngrediente());
            pstmt.setInt(2, nomeIngrediente.getIdNomeIngrediente());

            return pstmt.executeUpdate();
        }
    }

    // Deleta o nome pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from nome_ingrediente where id_nome = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }

    // Deleta todos os nomes do ingrediente escolhido.
    public int deletarPorIdIngrediente(int id) throws SQLException {
        String sql = "delete from nome_ingrediente where id_ingrediente = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }
}