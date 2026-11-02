package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.PerfilCabelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela perfil_cabelo: faz o CRUD do perfil de cabelo dos usuários.
public class PerfilCabeloDAO {

    // Cadastra o perfil de cabelo de um usuário.
    public boolean cadastrar(PerfilCabelo perfilCabelo) throws SQLException {
        String sql = "insert into perfil_cabelo(curvatura, oleosidade, espessura, id_usuario) values (?, ?, ?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, perfilCabelo.getCurvatura());
            pstmt.setInt(2, perfilCabelo.getOleosidade());
            pstmt.setInt(3, perfilCabelo.getEspessura());
            pstmt.setInt(4, perfilCabelo.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Lista todos os perfis de cabelo, em ordem de id.
    public ArrayList<PerfilCabelo> listar() throws SQLException {
        String sql = "select * from perfil_cabelo order by id_perfil_cabelo";
        ArrayList<PerfilCabelo> perfis = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
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

    // Busca um perfil de cabelo pelo id. Usado para preencher o formulário de alteração.
    public PerfilCabelo listarPorId(int id) throws SQLException {
        String sql = "select * from perfil_cabelo where id_perfil_cabelo = ?";
        PerfilCabelo perfil = null;

        try (Connection cnn = ConexaoBanco.conectar();
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

    // Busca o perfil de cabelo do usuário escolhido.
    public PerfilCabelo listarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "select * from perfil_cabelo where id_usuario = ?";
        PerfilCabelo perfil = null;

        try (Connection cnn = ConexaoBanco.conectar();
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

    // Altera a curvatura, a oleosidade e a espessura do perfil.
    public int alterar(PerfilCabelo perfilCabelo) throws SQLException {
        String sql = "update perfil_cabelo set curvatura = ?, oleosidade = ?, espessura = ? where id_perfil_cabelo = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, perfilCabelo.getCurvatura());
            pstmt.setInt(2, perfilCabelo.getOleosidade());
            pstmt.setInt(3, perfilCabelo.getEspessura());
            pstmt.setInt(4, perfilCabelo.getIdPerfilCabelo());

            return pstmt.executeUpdate();
        }
    }

    // Deleta o perfil de cabelo pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from perfil_cabelo where id_perfil_cabelo = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }

    // Deleta o perfil de cabelo do usuário escolhido.
    public int deletarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from perfil_cabelo where id_usuario = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();

        }
    }

    // Confere se o usuário já tem perfil de cabelo, porque cada usuário só pode ter um.
    public boolean existePerfilParaUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT 1 FROM perfil_cabelo WHERE id_usuario = ?";


        try (Connection conexao = ConexaoBanco.conectar();
        PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idUsuario);

            try (ResultSet resultado = comando.executeQuery()) {

                boolean existe = resultado.next();

                resultado.close();
                comando.close();
                conexao.close();

                return existe;
            }
        }
    }
}