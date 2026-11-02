package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.ProdutoUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela produto_usuario: liga os usuários aos produtos.
public class ProdutoUsuarioDAO {

    // Confere se o produto já está ligado ao usuário, para não cadastrar o mesmo vínculo duas vezes.
    public boolean existeVinculo(int idProduto, int idUsuario) throws SQLException {
        String sql = "select 1 from produto_usuario where id_produto = ? and id_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setInt(1, idProduto);
            pstm.setInt(2, idUsuario);

            try (ResultSet rset = pstm.executeQuery()) {
                return rset.next();
                //true se achou alguma linha, ou seja, o vínculo já existe.
            }
        }
    }

    // Cadastra o vínculo entre um produto e um usuário.
    public boolean cadastrar(ProdutoUsuario produtoUsuario) throws SQLException {
        String sql = "insert into produto_usuario (id_produto , id_usuario) values (?,?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setInt(1, produtoUsuario.getIdProduto());
            pstm.setInt(2, produtoUsuario.getIdUsuario());

            return pstm.executeUpdate() > 0;
        }
    }

    // Lista todos os vínculos. Os JOINs trazem o nome do produto e o nome do usuário para mostrar na tela.
    public ArrayList<ProdutoUsuario> listar() throws SQLException {
        String sql = "select pu.id_produto_usuario, pu.id_produto, pu.id_usuario, " +
                "p.nome as nome_produto, u.nome_completo as nome_usuario " +
                "from produto_usuario pu " +
                "join produto p on p.id_produto = pu.id_produto " +
                "join usuario u on u.id_usuario = pu.id_usuario " +
                "order by pu.id_produto_usuario";
        ArrayList<ProdutoUsuario> produtosUsuario = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                ProdutoUsuario prdUs = new ProdutoUsuario(
                        rset.getInt("id_produto_usuario"),
                        rset.getInt("id_produto"),
                        rset.getInt("id_usuario"));
                prdUs.setNomeProduto(rset.getString("nome_produto"));
                prdUs.setNomeUsuario(rset.getString("nome_usuario"));
                produtosUsuario.add(prdUs);
            }
        }
        return produtosUsuario;
    }

    // Busca um vínculo pelo id.
    public ProdutoUsuario listarPorId(int id) throws SQLException {
        String sql = "select * from produto_usuario where id_produto_usuario = ?";
        ProdutoUsuario produtoUsuario = null;

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    produtoUsuario = new ProdutoUsuario(
                            rset.getInt("id_produto_usuario"),
                            rset.getInt("id_produto"),
                            rset.getInt("id_usuario"));
                }
            }
        }
        return produtoUsuario;

    }

    // Lista os produtos ligados ao usuário escolhido, com o nome de cada produto.
    public ArrayList<ProdutoUsuario> listarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "select pu.id_produto_usuario, pu.id_produto, pu.id_usuario, " +
                "p.nome as nome_produto, u.nome_completo as nome_usuario " +
                "from produto_usuario pu " +
                "join produto p on p.id_produto = pu.id_produto " +
                "join usuario u on u.id_usuario = pu.id_usuario " +
                "where pu.id_usuario = ? " +
                "order by pu.id_produto_usuario";
        ArrayList<ProdutoUsuario> listaProdutoUsuario = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setInt(1, idUsuario);

            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    ProdutoUsuario pU1 = new ProdutoUsuario(
                            rset.getInt("id_produto_usuario"),
                            rset.getInt("id_produto"),
                            rset.getInt("id_usuario"));
                    pU1.setNomeProduto(rset.getString("nome_produto"));
                    pU1.setNomeUsuario(rset.getString("nome_usuario"));

                    listaProdutoUsuario.add(pU1);
                }
            }
        }
        return listaProdutoUsuario;

    }

    // Deleta o vínculo pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from produto_usuario where id_produto_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    // Deleta todos os vínculos do produto escolhido.
    public int deletarPorIdProduto(int id) throws SQLException {
        String sql = "delete from produto_usuario where id_produto = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    // Deleta todos os vínculos do usuário escolhido.
    public int deletarPorIdUsuario(int id) throws SQLException {
        String sql = "delete from produto_usuario where id_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }


}
