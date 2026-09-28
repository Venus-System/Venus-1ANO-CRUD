package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.ProdutoIngrediente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutoIngredienteDAO {

    public boolean cadastrarProdutoIngrediente(ProdutoIngrediente produtoIngrediente) throws SQLException {
        String sql = "insert into produto_ingrediente(id_ingrediente, id_produto) values (?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, produtoIngrediente.getIdIngrediente());
            pstmt.setInt(2, produtoIngrediente.getIdProduto());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<ProdutoIngrediente> read() throws SQLException {
        String sql = "select * from produto_ingrediente order by id_produto_ingrediente";
        ArrayList<ProdutoIngrediente> produtosIngrediente = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                ProdutoIngrediente pi1 = new ProdutoIngrediente(
                        rset.getInt("id_produto_ingrediente"),
                        rset.getInt("id_ingrediente"),
                        rset.getInt("id_produto")
                );
                produtosIngrediente.add(pi1);
            }
        } return produtosIngrediente;

    }

    public ProdutoIngrediente readById(int id) throws SQLException {
        String sql = "select * from produto_ingrediente where id_produto_ingrediente =?";
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

        } return produtoIngrediente;
    }


    public int deleteByIdIngrediente(int id) throws SQLException {
        String sql = "delete from produto_ingrediente where id_ingrediente = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }

    public int deleteByIdProduto(int id) throws SQLException {
        String sql = "delete from produto_ingrediente where id_produto = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }
}