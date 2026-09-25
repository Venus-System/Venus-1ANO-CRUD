package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.PerfilCabelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PerfilCabeloDAO {

    public boolean cadastrarPerfilCabelo(PerfilCabelo perfilCabelo) throws SQLException {
        String sql = "insert into perfil_cabelo(curvatura, oleosidade, espessura, id_usuario) values (?, ?, ?, ?)";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, perfilCabelo.getCurvatura());
            pstmt.setInt(2, perfilCabelo.getOleosidade());
            pstmt.setInt(3, perfilCabelo.getEspessura());
            pstmt.setInt(4, perfilCabelo.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<PerfilCabelo> read() throws SQLException {
        String sql = "select * from perfil_cabelo order by id_perfil_cabelo";
        ArrayList<PerfilCabelo> perfis = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                PerfilCabelo perfil1 = new PerfilCabelo(
                        rset.getInt("curvatura"),
                        rset.getInt("oleosidade"),
                        rset.getInt("espessura"),
                        rset.getInt("id_usuario"),
                        rset.getInt("id_perfil_cabelo")
                );
                perfis.add(perfil1);
            }
        }
        return perfis;
    }

    public PerfilCabelo readById(int id) throws SQLException {
        String sql = "select * from perfil_cabelo where id_perfil_cabelo = ?";
        PerfilCabelo perfil = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    perfil = new PerfilCabelo(
                            rset.getInt("curvatura"),
                            rset.getInt("oleosidade"),
                            rset.getInt("espessura"),
                            rset.getInt("id_usuario"),
                            rset.getInt("id_perfil_cabelo")
                    );
                }
            }

        }
        return perfil;
    }

    public PerfilCabelo readByIdUsuario(int idUsuario) throws SQLException {
        String sql = "select * from perfil_cabelo where id_usuario = ?";
        PerfilCabelo perfil = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    perfil = new PerfilCabelo(
                            rset.getInt("curvatura"),
                            rset.getInt("oleosidade"),
                            rset.getInt("espessura"),
                            rset.getInt("id_usuario"),
                            rset.getInt("id_perfil_cabelo")
                    );
                }
            }

        }
        return perfil;
    }

    public int alterarValores(PerfilCabelo perfilCabelo) throws SQLException {
        String sql = "update perfil_cabelo set curvatura = ?, oleosidade = ?, espessura = ? where id_perfil_cabelo = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, perfilCabelo.getCurvatura());
            pstmt.setInt(2, perfilCabelo.getOleosidade());
            pstmt.setInt(3, perfilCabelo.getEspessura());
            pstmt.setInt(4, perfilCabelo.getIdPerfilCabelo());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from perfil_cabelo where id_perfil_cabelo = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }

    public int deleteByIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from perfil_cabelo where id_usuario = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();

        }
    }

    public boolean existePerfilParaUsuario(int idUsuario) throws SQLException {
        Connection conexao = new ConexaoBanco().conectar();

        String sql = "SELECT 1 FROM perfil_cabelo WHERE id_usuario = ?";
        PreparedStatement comando = conexao.prepareStatement(sql);

        comando.setInt(1, idUsuario);

        ResultSet resultado = comando.executeQuery();

        boolean existe = resultado.next();

        resultado.close();
        comando.close();
        conexao.close();

        return existe;
    }
}