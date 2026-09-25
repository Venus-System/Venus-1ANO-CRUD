package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Analise;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnaliseDAO {
    public boolean cadastrarAnalise(Analise analise) throws SQLException {
        String sql = "insert into analise (dt_hr_analise, resumo_resultado, pontuacao, id_usuario) values (?, ?, ?, ?)";

        try (Connection cnn= new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)){
            pstmt.setObject(1, analise.getDtHrAnalise());
            pstmt.setString( 2, analise.getResumoResultado());
            pstmt.setInt(3, analise.getPontuacao());
            pstmt.setInt(4, analise.getIdUsuario());

            return pstmt.executeUpdate()>0;
            // o executeUpdate so vai retornar quantas linhas do banco foram alteradas, não retorna os dados inseridos.
        }
    }

    public ArrayList<Analise> read() throws SQLException {
        String sql = "select * from analise order by id_analise";
        ArrayList<Analise> analise = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                Analise an1 = new Analise(
                    rset.getInt("id_analise"),
                    rset.getString("dt_hr_analise"),
                    rset.getString( "resumo_resultado"),
                    rset.getInt("pontuacao"),
                    rset.getInt("id_usuario")

                );
                analise.add(an1);
            }
        } return analise;

    }

    public Analise readById(int id) throws SQLException{
        String sql = "select * from analise where id_analise = ?";
        Analise analise = null;
        //ainda sem objeto
        try(Connection cnn = new ConexaoBanco().conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setInt(1,id);

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                if(rset.next()){
                    analise = new Analise(
                            rset.getInt("id_analise"),
                            rset.getString("dt_hr_analise"),
                            rset.getString( "resumo_resultado"),
                            rset.getInt("pontuacao"),
                            rset.getInt("id_usuario")

                            //retornará a analise com o id que está sendo procurado.
                    );
                }
            }

        } return analise;
    }
    public int update (Analise analise) throws SQLException {
        String sql = "update analise set dt_hr_analise = ?, resumo_resultado = ?, pontuacao =? , id_usuario = ? where id_analise = ? ";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)){

            pstmt.setObject(1, analise.getDtHrAnalise());
            pstmt.setString( 2, analise.getResumoResultado());
            pstmt.setInt(3, analise.getPontuacao());
            pstmt.setInt(4, analise.getIdUsuario());
            pstmt.setInt(5, analise.getIdAnalise());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from analise where id_analise = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }

    public int deleteByIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from analise where id_usuario = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();

        }
    }
}
