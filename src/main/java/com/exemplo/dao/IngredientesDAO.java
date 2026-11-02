package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Ingredientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

// DAO da tabela ingredientes: faz o CRUD dos ingredientes e do nível de perigo deles.
public class IngredientesDAO {

    // Cadastra um novo ingrediente com o tipo, o nível de perigo e a data de atualização.
    public boolean cadastrar(Ingredientes ingredientes) throws SQLException {
        String sql = "insert into ingredientes(nivel_perigo, tipo, dt_atualizacao) values (?, ?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, ingredientes.getNivelPerigo());
            pstmt.setString(2, ingredientes.getTipo());
            pstmt.setObject(3, LocalDate.now());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Lista todos os ingredientes, em ordem de id.
    public ArrayList<Ingredientes> listar() throws SQLException {
        String sql = "select * from ingredientes order by id_ingrediente";
        ArrayList<Ingredientes> ingredientes = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                Ingredientes ing1 = new Ingredientes(
                        rset.getInt("id_ingrediente"),
                        rset.getInt("nivel_perigo"),
                        rset.getString("tipo"),
                        rset.getObject("dt_atualizacao", LocalDate.class)
                );
                ingredientes.add(ing1);
            }
        } return ingredientes;

    }

    // Busca um ingrediente pelo id. Usado para preencher o formulário de alteração.
    public Ingredientes listarPorId(int id) throws SQLException {
        String sql = "select * from ingredientes where id_ingrediente =?";
        Ingredientes ingredientes = null;

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    ingredientes = new Ingredientes(
                            rset.getInt("id_ingrediente"),
                            rset.getInt("nivel_perigo"),
                            rset.getString("tipo"),
                            rset.getObject("dt_atualizacao", LocalDate.class)
                    );
                }
            }

        } return ingredientes;
    }

    // Altera o tipo, o nível de perigo e a data de atualização do ingrediente.
    public int alterar(Ingredientes ingredientes) throws SQLException {
        String sql = "update ingredientes set nivel_perigo = ?, tipo = ?, dt_atualizacao = ? where id_ingrediente = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, ingredientes.getNivelPerigo());
            pstmt.setString(2, ingredientes.getTipo());
            pstmt.setObject(3,LocalDate.now());
            pstmt.setInt(4, ingredientes.getIdIngrediente());

            return pstmt.executeUpdate();
        }
    }

    // Deleta o ingrediente pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from ingredientes where id_ingrediente = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }
}