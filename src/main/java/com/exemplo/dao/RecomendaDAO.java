package com.exemplo.dao;

import com.exemplo.model.Recomenda;
import com.exemplo.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecomendaDAO {

    private static final String SELECT_COMPLETO =
            "select r.id_produto, r.id_usuario, p.nome, p.marca, u.nome_completo " +
            "from recomenda r " +
            "left join produto p on p.id_produto = r.id_produto " +
            "left join usuario u on u.id_usuario = r.id_usuario ";

    public boolean cadastrarRecomenda(Recomenda recomenda) throws SQLException {
        String sql = "insert into recomenda (id_produto, id_usuario) values (?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, recomenda.getIdProduto());
            pstmt.setInt(2, recomenda.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<Recomenda> read() throws SQLException {
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

    public Recomenda readByChave(int idUsuario, int idProduto) throws SQLException {
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

    public ArrayList<Recomenda> readByIdUsuario(int idUsuario) throws SQLException {
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

    public int update(int idUsuario, int idProdutoAntigo, int idProdutoNovo) throws SQLException {
        String sql = "update recomenda set id_produto = ? where id_usuario = ? and id_produto = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idProdutoNovo);
            pstmt.setInt(2, idUsuario);
            pstmt.setInt(3, idProdutoAntigo);
            return pstmt.executeUpdate();
        }
    }

    public int delete(int idUsuario, int idProduto) throws SQLException {
        String sql = "delete from recomenda where id_usuario = ? and id_produto = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idProduto);
            return pstmt.executeUpdate();
        }
    }

    public int deleteByIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from recomenda where id_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();
        }
    }

    //apaga as recomendações do usuário e escolhe de novo os 5 produtos com maior pontuação
    //que combinam com as preferências dele (categoria ou marca favorita; se prefere vegano, só veganos).
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

    private Recomenda montar(ResultSet rset) throws SQLException {
        Recomenda recomenda = new Recomenda(rset.getInt("id_produto"), rset.getInt("id_usuario"));
        recomenda.setNomeProduto(rset.getString("nome"));
        recomenda.setMarcaProduto(rset.getString("marca"));
        recomenda.setNomeUsuario(rset.getString("nome_completo"));
        return recomenda;
    }
}
