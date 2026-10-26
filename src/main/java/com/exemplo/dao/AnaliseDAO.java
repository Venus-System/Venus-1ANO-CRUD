package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Analise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AnaliseDAO {
    // Registra a data e a hora atuais no cadastro da análise.
    public boolean cadastrarAnalise(Analise analise) throws SQLException {
        String sql = "insert into analise (dt_hr_analise, resumo_resultado, pontuacao, id_usuario) values (?, ?, ?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setObject(1, LocalDateTime.now());
            pstmt.setString(2, analise.getResumoResultado());
            pstmt.setInt(3, analise.getPontuacao());
            pstmt.setInt(4, analise.getIdUsuario());

            // O cadastro retorna apenas se houve inserção, sem recuperar os dados gerados pelo banco.
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<Analise> read() throws SQLException {
        String sql = "select * from analise order by id_analise";
        ArrayList<Analise> analise = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                Analise an1 = new Analise(
                        rset.getInt("id_analise"),
                        rset.getObject("dt_hr_analise", LocalDateTime.class),
                        rset.getString("resumo_resultado"),
                        rset.getInt("pontuacao"),
                        rset.getInt("id_usuario")

                );
                analise.add(an1);
            }
        }
        return analise;

    }

    public Analise readById(int id) throws SQLException {
        String sql = "select * from analise where id_analise = ?";
        Analise analise = null;
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    analise = new Analise(
                            rset.getInt("id_analise"),
                            rset.getObject("dt_hr_analise", LocalDateTime.class),
                            rset.getString("resumo_resultado"),
                            rset.getInt("pontuacao"),
                            rset.getInt("id_usuario")

                    );
                }
            }

        }
        return analise;
    }

    public ArrayList<Analise> readByIdUsuario(int idUsuario) throws SQLException {
        String sql = "select * from analise where id_usuario = ? order by id_analise";
        ArrayList<Analise> lista = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    lista.add(new Analise(
                            rset.getInt("id_analise"),
                            rset.getObject("dt_hr_analise", LocalDateTime.class),
                            rset.getString("resumo_resultado"),
                            rset.getInt("pontuacao"),
                            rset.getInt("id_usuario")
                    ));
                }
            }
        }
        return lista;
    }

    // Atualiza também a data e a hora da análise, mantendo o usuário vinculado.
    public int update(Analise analise) throws SQLException {
        String sql = "update analise set dt_hr_analise = ?, resumo_resultado = ?, pontuacao =?  where id_analise = ? ";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setObject(1, LocalDateTime.now());
            pstmt.setString(2, analise.getResumoResultado());
            pstmt.setInt(3, analise.getPontuacao());
            pstmt.setInt(4, analise.getIdAnalise());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from analise where id_analise = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }

    // Exclui todo o histórico de análises do usuário informado.
    public int deleteByIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from analise where id_usuario = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();
        }
    }
}
