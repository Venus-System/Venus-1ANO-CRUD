package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.PerfilPele;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PerfilPeleDAO {

    public boolean cadastrarPerfilPele(PerfilPele perfilPele) throws SQLException {
        String sql = "insert into perfil_pele(sensibilidade, tipo_pele, nivel_oleosidade, id_usuario) values (?, ?, ?, ?)";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, perfilPele.getSensibilidade());
            pstmt.setString(2, perfilPele.getTipoPele());
            pstmt.setInt(3, perfilPele.getNivelOleosidade());
            pstmt.setInt(4, perfilPele.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ArrayList<PerfilPele> read() throws SQLException {
        String sql = "select * from perfil_pele order by id_perfil_pele";
        ArrayList<PerfilPele> perfis = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                PerfilPele perfil1 = new PerfilPele(
                        rset.getInt("sensibilidade"),
                        rset.getString("tipo_pele"),
                        rset.getInt("nivel_oleosidade"),
                        rset.getInt("id_usuario"),
                        rset.getInt("id_perfil_pele")
                );
                perfis.add(perfil1);
            }
        }
        return perfis;
    }

    public PerfilPele readById(int id) throws SQLException {
        String sql = "select * from perfil_pele where id_perfil_pele = ?";
        PerfilPele perfil = null;
        //ainda sem objeto
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                //que permite a visualização das tabelas
                if (rset.next()) {
                    perfil = new PerfilPele(
                            rset.getInt("sensibilidade"),
                            rset.getString("tipo_pele"),
                            rset.getInt("nivel_oleosidade"),
                            rset.getInt("id_usuario"),
                            rset.getInt("id_perfil_pele")
                    );
                }
            }

        }
        return perfil;
    }

    public PerfilPele readByIdUsuario(int idUsuario) throws SQLException {
        String sql = "select * from perfil_pele where id_usuario = ?";
        PerfilPele perfil = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    perfil = new PerfilPele(
                            rset.getInt("sensibilidade"),
                            rset.getString("tipo_pele"),
                            rset.getInt("nivel_oleosidade"),
                            rset.getInt("id_usuario"),
                            rset.getInt("id_perfil_pele")
                    );
                }
            }

        }
        return perfil;
    }

    public int alterarValores(PerfilPele perfilPele) throws SQLException {
        String sql = "update perfil_pele set sensibilidade = ?, tipo_pele = ?, nivel_oleosidade = ? where id_perfil_pele = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, perfilPele.getSensibilidade());
            pstmt.setString(2, perfilPele.getTipoPele());
            pstmt.setInt(3, perfilPele.getNivelOleosidade());
            pstmt.setInt(4, perfilPele.getIdPerfilPele());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from perfil_pele where id_perfil_pele = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }

    public int deleteByIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from perfil_pele where id_usuario= ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();

        }
    }


    public boolean existePerfilParaUsuario(int idUsuario) throws SQLException {
        Connection conexao = new ConexaoBanco().conectar();

        String sql = "SELECT 1 FROM perfil_pele WHERE id_usuario = ?";
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