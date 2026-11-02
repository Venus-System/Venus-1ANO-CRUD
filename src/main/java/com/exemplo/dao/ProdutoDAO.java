package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela produto: faz o CRUD dos produtos.
public class ProdutoDAO {
    // Cadastra um novo produto.
    public boolean cadastrar(Produto produto) throws SQLException{
        String sql = "insert into produto (nome, marca, categoria, descricao, eh_vegano, eh_cruelty_free, pontuacao, lista_ingredientes) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cnn= ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)){
            pstmt.setString(1,produto.getNome());
            pstmt.setString(2, produto.getMarca());
            pstmt.setString(3,produto.getCategoria());
            pstmt.setString(4, produto.getDescricao());
            pstmt.setBoolean(5, produto.getEhVegano());
            pstmt.setBoolean(6, produto.getEhCrueltyFree());
            pstmt.setInt(7, produto.getPontuacao());
            pstmt.setString(8, produto.getListaIngredientes());

            return pstmt.executeUpdate()>0;
            // o executeUpdate so vai retornar quantas linhas do banco foram alteradas, não retorna os dados inseridos.
        }
    }

    // Lista todos os produtos, em ordem de id.
    public ArrayList<Produto> listar() throws SQLException {
        String sql = "select * from produto order by id_produto";
        ArrayList<Produto> produtos = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                Produto p1 = new Produto(
                    rset.getInt("id_produto"),
                    rset.getString("nome"),
                    rset.getString("marca"),
                    rset.getString("categoria"),
                    rset.getString("descricao"),
                    rset.getBoolean("eh_vegano"),
                    rset.getBoolean("eh_cruelty_free"),
                    rset.getInt("pontuacao"),
                    rset.getString("lista_ingredientes")

                );
                produtos.add(p1);
            }
        } return produtos;

    }

    // Busca um produto pelo id. Usado para preencher o formulário de alteração.
    public Produto listarPorId(int id) throws SQLException{
        String sql = "select * from produto where id_produto = ?";
        Produto produto = null;
        //ainda sem objeto
        try(Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setInt(1,id);

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                if(rset.next()){
                    produto = new Produto(
                            rset.getInt("id_produto"),
                            rset.getString("nome"),
                            rset.getString("marca"),
                            rset.getString("categoria"),
                            rset.getString("descricao"),
                            rset.getBoolean("eh_vegano"),
                            rset.getBoolean("eh_cruelty_free"),
                            rset.getInt("pontuacao"),
                            rset.getString("lista_ingredientes")

                    );
                }
            }

        } return produto;
    }

    // Lista os produtos cujo nome contém o texto pesquisado (filtro da tela).
    public ArrayList<Produto> listarPorNome(String nome) throws SQLException{
        String sql = "select * from produto where nome like ?";
        ArrayList<Produto> listaProdutos =  new ArrayList<>();

        try(Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setString(1,"%"+nome+"%");

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                while(rset.next()){
                    Produto p1 = new Produto(
                            rset.getInt("id_produto"),
                            rset.getString("nome"),
                            rset.getString("marca"),
                            rset.getString("categoria"),
                            rset.getString("descricao"),
                            rset.getBoolean("eh_vegano"),
                            rset.getBoolean("eh_cruelty_free"),
                            rset.getInt("pontuacao"),
                            rset.getString("lista_ingredientes")

                    );

                    listaProdutos.add(p1);
                }
            }

        } return listaProdutos;
    }

    // Lista os produtos da marca pesquisada (filtro da tela).
    public ArrayList<Produto> listarPorMarca(String marca) throws SQLException{
        String sql = "select * from produto where marca like ?";
        ArrayList<Produto> listaProdutosMarca = new ArrayList<>();

        try(Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setString(1,"%"+marca+"%");

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                while (rset.next()){
                    Produto p1 = new Produto(
                            rset.getInt("id_produto"),
                            rset.getString("nome"),
                            rset.getString("marca"),
                            rset.getString("categoria"),
                            rset.getString("descricao"),
                            rset.getBoolean("eh_vegano"),
                            rset.getBoolean("eh_cruelty_free"),
                            rset.getInt("pontuacao"),
                            rset.getString("lista_ingredientes")
                    );
                    listaProdutosMarca.add(p1);
                }
            }

        } return listaProdutosMarca;
    }

    // Altera todos os dados do produto escolhido.
    public int alterar(Produto produto) throws SQLException {
        String sql = "update produto set nome =? , marca =?, categoria =?, descricao =?, eh_vegano =?, eh_cruelty_free =?, pontuacao =?, lista_ingredientes= ? where id_produto = ? ";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)){

            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getMarca());
            pstmt.setString(3, produto.getCategoria());
            pstmt.setString(4, produto.getDescricao());
            pstmt.setBoolean(5, produto.getEhVegano());
            pstmt.setBoolean(6, produto.getEhCrueltyFree());
            pstmt.setInt(7, produto.getPontuacao());
            pstmt.setString(8, produto.getListaIngredientes());
            pstmt.setInt(9, produto.getIdProduto());

            return pstmt.executeUpdate();
        }
    }


    // Deleta o produto pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from produto where id_produto = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }
}
