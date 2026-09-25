package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutoUsuarioDAO {

    public boolean inserirProdutoUsuario(ProdutoUsuario produtoUsuario) throws SQLException {
        String sql= "insert into produto_usuario (id_produto , id_usuario) values (?,?)";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1, produtoUsuario.getIdProduto());
            pstm.setInt(2, produtoUsuario.getIdUsuario());

            return pstm.executeUpdate()>0;
        }
    }

    public ArrayList<ProdutoUsuario> read() throws SQLException{
        String sql= "select*from produto_usuario order by id_produto_usuario";
        ArrayList<ProdutoUsuario> produtoUsuario = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()){
                ProdutoUsuario prdUs = new ProdutoUsuario (
                    rset.getInt("id_produto_usuario"),
                    rset.getInt("id_produto"),
                    rset.getInt("id_usuario"));
                produtoUsuario.add(prdUs);
            }
        } return produtoUsuario;
    }

    public ProdutoUsuario readById(int id) throws SQLException{
        String sql= "select*from produto_usuario where id_produto_usuario = ?";
        ProdutoUsuario produtoUsuario = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
             pstm.setInt(1, id);

            try (ResultSet rset = pstm.executeQuery()) {
                if(rset.next()){
                    produtoUsuario = new ProdutoUsuario(
                            rset.getInt("id_produto_usuario"),
                            rset.getInt("id_produto"),
                            rset.getInt("id_usuario"));
                }
            }
        }return produtoUsuario;

    }

    public int update (ProdutoUsuario produtoUsuario) throws SQLException {
        String sql = "update produto_usuario set id_produto = ?, id_usuario =?  where id_produto_usuario = ? ";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)){

            pstmt.setInt(1,produtoUsuario.getIdProduto());
            pstmt.setInt(2,produtoUsuario.getIdUsuario());
            pstmt.setInt(3,produtoUsuario.getIdProdutoUsuario());


            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException{
        String sql = "delete from produto_usuario where id_produto_usuario = ?";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    public int deleteByIdProduto(int id) throws SQLException{
        String sql = "delete from produto_usuario where id_produto = ?";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    public int deleteByIdUsuario(int id) throws SQLException{
        String sql = "delete from produto_usuario where id_usuario = ?";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }


}
