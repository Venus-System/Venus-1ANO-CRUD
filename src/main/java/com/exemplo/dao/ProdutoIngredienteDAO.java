package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.ProdutoIngrediente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela produto_ingrediente: liga cada produto aos ingredientes que ele tem.
public class ProdutoIngredienteDAO {

    // Cadastra um ingrediente em um produto.
    public boolean cadastrar(ProdutoIngrediente produtoIngrediente) throws SQLException {
        String sql = "insert into produto_ingrediente(id_ingrediente, id_produto) values (?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, produtoIngrediente.getIdIngrediente());
            pstmt.setInt(2, produtoIngrediente.getIdProduto());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Lista os ingredientes de todos os produtos. Os JOINs trazem o tipo, o nível de perigo e os nomes do ingrediente, juntados em um texto só pelo string_agg.
    public ArrayList<ProdutoIngrediente> listar() throws SQLException {
        String sql = "SELECT pi.id_produto_ingrediente, pi.id_produto, pi.id_ingrediente, "
                + "       COALESCE(string_agg(ni.nome_ingrediente, ', '), '(sem nome)') AS nome_ingrediente, "
                + "       i.nivel_perigo, i.tipo "
                + "FROM produto_ingrediente pi "
                + "JOIN ingredientes i ON i.id_ingrediente = pi.id_ingrediente "
                + "LEFT JOIN nome_ingrediente ni ON ni.id_ingrediente = i.id_ingrediente "
                + "GROUP BY pi.id_produto_ingrediente, pi.id_produto, pi.id_ingrediente, i.nivel_perigo, i.tipo "
                + "ORDER BY pi.id_produto, pi.id_produto_ingrediente";
        ArrayList<ProdutoIngrediente> produtosIngrediente = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                ProdutoIngrediente pi1 = new ProdutoIngrediente(
                        rset.getInt("id_produto_ingrediente"),
                        rset.getInt("id_produto"),
                        rset.getInt("id_ingrediente"),
                        rset.getString("nome_ingrediente"),
                        rset.getInt("nivel_perigo"),
                        rset.getString("tipo"));
                produtosIngrediente.add(pi1);
            }
        }
        return produtosIngrediente;

    }

    // Lista os ingredientes do produto escolhido, com os nomes, o tipo e o nível de perigo.
    public ArrayList<ProdutoIngrediente> listarPorIdProduto(int id) throws SQLException {
        String sql = "SELECT pi.id_produto_ingrediente, pi.id_produto, pi.id_ingrediente, "
                + "       COALESCE(string_agg(ni.nome_ingrediente, ', '), '(sem nome)') AS nome_ingrediente, "
                + "       i.nivel_perigo, i.tipo "
                + "FROM produto_ingrediente pi "
                + "JOIN ingredientes i ON i.id_ingrediente = pi.id_ingrediente "
                + "LEFT JOIN nome_ingrediente ni ON ni.id_ingrediente = i.id_ingrediente "
                + "WHERE pi.id_produto = ? "
                + "GROUP BY pi.id_produto_ingrediente, pi.id_produto, pi.id_ingrediente, i.nivel_perigo, i.tipo "
                + "ORDER BY pi.id_produto_ingrediente";
        ArrayList<ProdutoIngrediente> listaProdutosIngredientes = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente(
                            rset.getInt("id_produto_ingrediente"),
                            rset.getInt("id_produto"),
                            rset.getInt("id_ingrediente"),
                            rset.getString("nome_ingrediente"),
                            rset.getInt("nivel_perigo"),
                            rset.getString("tipo"));
                    listaProdutosIngredientes.add(produtoIngrediente);
                }
            }

        }
        return listaProdutosIngredientes;
    }

    // Busca um vínculo entre produto e ingrediente pelo id.
    public ProdutoIngrediente listarPorId(int id) throws SQLException {
        String sql = "select * from produto_ingrediente where id_produto_ingrediente = ?";
        ProdutoIngrediente produtoIngrediente = null;

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    produtoIngrediente = new ProdutoIngrediente(
                            rset.getInt("id_produto_ingrediente"),
                            rset.getInt("id_ingrediente"),
                            rset.getInt("id_produto")
                    );
                }
            }

        }
        return produtoIngrediente;
    }

    // Deleta o vínculo pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from produto_ingrediente where id_produto_ingrediente = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }

    // Deleta o ingrediente escolhido de todos os produtos.
    public int deletarPorIdIngrediente(int id) throws SQLException {
        String sql = "delete from produto_ingrediente where id_ingrediente = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }

    // Deleta todos os ingredientes ligados ao produto escolhido.
    public int deletarPorIdProduto(int id) throws SQLException {
        String sql = "delete from produto_ingrediente where id_produto = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }
}