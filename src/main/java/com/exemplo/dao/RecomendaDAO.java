package com.exemplo.dao;

import com.exemplo.model.Recomenda;
import com.exemplo.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela recomenda: guarda os produtos recomendados para cada usuário.
public class RecomendaDAO {

    // SELECT usado nas listagens. Os LEFT JOINs trazem o nome e a marca do produto e o nome do usuário.
    private static final String SELECT_COMPLETO =
            "select r.id_produto, r.id_usuario, p.nome, p.marca, u.nome_completo " +
            "from recomenda r " +
            "left join produto p on p.id_produto = r.id_produto " +
            "left join usuario u on u.id_usuario = r.id_usuario ";

    // Cadastra uma recomendação de produto para um usuário.
    public boolean cadastrar(Recomenda recomenda) throws SQLException {
        String sql = "insert into recomenda (id_produto, id_usuario) values (?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, recomenda.getIdProduto());
            pstmt.setInt(2, recomenda.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Lista todas as recomendações, com o nome do produto e do usuário.
    public ArrayList<Recomenda> listar() throws SQLException {
        String sql = SELECT_COMPLETO + "order by r.id_usuario, r.id_produto";
        ArrayList<Recomenda> lista = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                lista.add(montar(rset));
            }
        }
        return lista;
    }

    // Busca uma recomendação pela chave dupla (id do usuário + id do produto).
    public Recomenda listarPorChave(int idUsuario, int idProduto) throws SQLException {
        String sql = SELECT_COMPLETO + "where r.id_usuario = ? and r.id_produto = ?";
        Recomenda recomenda = null;

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idProduto);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    recomenda = montar(rset);
                }
            }
        }
        return recomenda;
    }

    // Lista os produtos recomendados para o usuário escolhido.
    public ArrayList<Recomenda> listarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = SELECT_COMPLETO + "where r.id_usuario = ? order by r.id_produto";
        ArrayList<Recomenda> lista = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    lista.add(montar(rset));
                }
            }
        }
        return lista;
    }

    // Troca o produto recomendado para o usuário: o antigo sai e o novo entra no lugar.
    public int alterar(int idUsuario, int idProdutoAntigo, int idProdutoNovo) throws SQLException {
        String sql = "update recomenda set id_produto = ? where id_usuario = ? and id_produto = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idProdutoNovo);
            pstmt.setInt(2, idUsuario);
            pstmt.setInt(3, idProdutoAntigo);
            return pstmt.executeUpdate();
        }
    }

    // Deleta a recomendação do usuário e do produto escolhidos.
    public int deletar(int idUsuario, int idProduto) throws SQLException {
        String sql = "delete from recomenda where id_usuario = ? and id_produto = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idProduto);
            return pstmt.executeUpdate();
        }
    }

    // Deleta todas as recomendações do usuário escolhido.
    public int deletarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from recomenda where id_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();
        }
    }

    // Refaz as recomendações do usuário: apaga as antigas e grava até 5 produtos que combinam com as preferências dele (categoria, marca e vegano), começando pelos de maior pontuação.
    public int recomendarPelasPreferencias(int idUsuario) throws SQLException {
        String sqlApagar = "delete from recomenda where id_usuario = ?";
        String sqlInserir = "insert into recomenda (id_produto, id_usuario) "
                + "select id_produto, ? from ( "
                + "  select distinct pr.id_produto, pr.pontuacao "
                + "  from produto pr "
                + "  join preferencias pf on pf.id_usuario = ? "
                + "  where ((pf.categorias_pref <> '' and (pr.categoria ilike '%' || pf.categorias_pref || '%' "
                + "                                     or pf.categorias_pref ilike '%' || pr.categoria || '%')) "
                + "      or (pf.marcas_fav <> '' and pr.marca ilike pf.marcas_fav)) "
                + "    and (pf.prefere_vegano = false or pr.eh_vegano = true) "
                + "  order by pr.pontuacao desc "
                + "  limit 5 "
                + ") escolhidos";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement apagar = cnn.prepareStatement(sqlApagar);
             PreparedStatement inserir = cnn.prepareStatement(sqlInserir)) {
            apagar.setInt(1, idUsuario);
            apagar.executeUpdate();

            inserir.setInt(1, idUsuario);
            inserir.setInt(2, idUsuario);
            return inserir.executeUpdate();
        }
    }

    // Monta o objeto Recomenda a partir da linha do banco, para não repetir esse código nas listagens.
    private Recomenda montar(ResultSet rset) throws SQLException {
        Recomenda recomenda = new Recomenda(rset.getInt("id_produto"), rset.getInt("id_usuario"));
        recomenda.setNomeProduto(rset.getString("nome"));
        recomenda.setMarcaProduto(rset.getString("marca"));
        recomenda.setNomeUsuario(rset.getString("nome_completo"));
        return recomenda;
    }
}
