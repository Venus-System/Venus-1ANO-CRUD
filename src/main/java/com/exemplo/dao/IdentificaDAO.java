package com.exemplo.dao;

import com.exemplo.model.Identifica;
import com.exemplo.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IdentificaDAO {

    private static final String SELECT_COM_INGREDIENTE =
            "select ide.id_ingrediente, ide.id_analise, i.tipo, i.nivel_perigo " +
            "from identifica ide " +
            "left join ingredientes i on i.id_ingrediente = ide.id_ingrediente ";

    public boolean cadastrarIdentifica(Identifica identifica) throws SQLException {
        String sql = "insert into identifica (id_ingrediente, id_analise) values (?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, identifica.getIdIngrediente());
            pstmt.setInt(2, identifica.getIdAnalise());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<Identifica> read() throws SQLException {
        String sql = SELECT_COM_INGREDIENTE + "order by ide.id_analise, ide.id_ingrediente";
        ArrayList<Identifica> lista = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                lista.add(montar(rset));
            }
        }
        return lista;
    }

    public Identifica readByChave(int idAnalise, int idIngrediente) throws SQLException {
        String sql = SELECT_COM_INGREDIENTE + "where ide.id_analise = ? and ide.id_ingrediente = ?";
        Identifica identifica = null;

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnalise);
            pstmt.setInt(2, idIngrediente);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    identifica = montar(rset);
                }
            }
        }
        return identifica;
    }

    public ArrayList<Identifica> readByIdAnalise(int idAnalise) throws SQLException {
        String sql = SELECT_COM_INGREDIENTE + "where ide.id_analise = ? order by ide.id_ingrediente";
        ArrayList<Identifica> lista = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnalise);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    lista.add(montar(rset));
                }
            }
        }
        return lista;
    }

    public int update(int idAnalise, int idIngredienteAntigo, int idIngredienteNovo) throws SQLException {
        String sql = "update identifica set id_ingrediente = ? where id_analise = ? and id_ingrediente = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idIngredienteNovo);
            pstmt.setInt(2, idAnalise);
            pstmt.setInt(3, idIngredienteAntigo);
            return pstmt.executeUpdate();
        }
    }

    public int delete(int idAnalise, int idIngrediente) throws SQLException {
        String sql = "delete from identifica where id_analise = ? and id_ingrediente = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnalise);
            pstmt.setInt(2, idIngrediente);
            return pstmt.executeUpdate();
        }
    }

    public int deleteByIdAnalise(int idAnalise) throws SQLException {
        String sql = "delete from identifica where id_analise = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnalise);
            return pstmt.executeUpdate();
        }
    }

    private Identifica montar(ResultSet rset) throws SQLException {
        Identifica identifica = new Identifica(rset.getInt("id_ingrediente"), rset.getInt("id_analise"));
        identifica.setTipoIngrediente(rset.getString("tipo"));
        identifica.setNivelPerigo(rset.getInt("nivel_perigo"));
        return identifica;
    }
}
