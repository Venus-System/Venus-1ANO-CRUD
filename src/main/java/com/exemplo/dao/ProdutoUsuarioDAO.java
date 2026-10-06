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

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1, produtoUsuario.getIdProduto());
            pstm.setInt(2, produtoUsuario.getIdUsuario());

            return pstm.executeUpdate()>0;
        }
    }

    public ArrayList<ProdutoUsuario> read() throws SQLException{
        String sql= "select*from produto_usuario order by id_produto_usuario";
        ArrayList<ProdutoUsuario> produtosUsuario = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()){
                ProdutoUsuario prdUs = new ProdutoUsuario (
                    rset.getInt("id_produto_usuario"),
                    rset.getInt("id_produto"),
                    rset.getInt("id_usuario"));
                produtosUsuario.add(prdUs);
            }
        } return produtosUsuario;
    }

    public ProdutoUsuario readById(int id) throws SQLException{
        String sql= "select * from produto_usuario where id_produto_usuario = ?";
        ProdutoUsuario produtoUsuario = null;

        try (Connection cnn = ConexaoBanco.conectar();
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

    public ArrayList<ProdutoUsuario> readByIdUsuario(int idUsuario) throws SQLException{
        String sql= "select * from produto_usuario where id_usuario = ?";
        ArrayList<ProdutoUsuario> listaProdutoUsuario = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1, idUsuario);

            try (ResultSet rset = pstm.executeQuery()) {
                while(rset.next()){
                    ProdutoUsuario pU1 = new ProdutoUsuario(
                            rset.getInt("id_produto_usuario"),
                            rset.getInt("id_produto"),
                            rset.getInt("id_usuario"));

                    listaProdutoUsuario.add(pU1);
                }
            }
        }return listaProdutoUsuario;

    }

    public int deleteById(int id) throws SQLException{
        String sql = "delete from produto_usuario where id_produto_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    public int deleteByIdProduto(int id) throws SQLException{
        String sql = "delete from produto_usuario where id_produto = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    public int deleteByIdUsuario(int id) throws SQLException{
        String sql = "delete from produto_usuario where id_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }


}
