package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.PerfilPele;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela perfil_pele: faz o CRUD do perfil de pele dos usuários.
public class PerfilPeleDAO {

    // Cadastra o perfil de pele de um usuário.
    public boolean cadastrar(PerfilPele perfilPele) throws SQLException {
        String sql = "insert into perfil_pele(sensibilidade, tipo_pele, nivel_oleosidade, id_usuario) values (?, ?, ?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setInt(1, perfilPele.getSensibilidade());
            pstmt.setString(2, perfilPele.getTipoPele());
            pstmt.setInt(3, perfilPele.getNivelOleosidade());
            pstmt.setInt(4, perfilPele.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Lista todos os perfis de pele, em ordem de id.
    public ArrayList<PerfilPele> listar() throws SQLException {
        String sql = "select * from perfil_pele order by id_perfil_pele";
        ArrayList<PerfilPele> perfis = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
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

    // Busca um perfil de pele pelo id. Usado para preencher o formulário de alteração.
    public PerfilPele listarPorId(int id) throws SQLException {
        String sql = "select * from perfil_pele where id_perfil_pele = ?";
        PerfilPele perfil = null;
        //ainda sem objeto
        try (Connection cnn = ConexaoBanco.conectar();
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

    // Busca o perfil de pele do usuário escolhido.
    public PerfilPele listarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "select * from perfil_pele where id_usuario = ?";
        PerfilPele perfil = null;

        try (Connection cnn = ConexaoBanco.conectar();
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

    // Altera a sensibilidade, o tipo de pele e a oleosidade do perfil.
    public int alterar(PerfilPele perfilPele) throws SQLException {
        String sql = "update perfil_pele set sensibilidade = ?, tipo_pele = ?, nivel_oleosidade = ? where id_perfil_pele = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, perfilPele.getSensibilidade());
            pstmt.setString(2, perfilPele.getTipoPele());
            pstmt.setInt(3, perfilPele.getNivelOleosidade());
            pstmt.setInt(4, perfilPele.getIdPerfilPele());

            return pstmt.executeUpdate();
        }
    }

    // Deleta o perfil de pele pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from perfil_pele where id_perfil_pele = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        }
    }

    // Deleta o perfil de pele do usuário escolhido.
    public int deletarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from perfil_pele where id_usuario= ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();

        }
    }


    // Confere se o usuário já tem perfil de pele, porque cada usuário só pode ter um.
    public boolean existePerfilParaUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT 1 FROM perfil_pele WHERE id_usuario = ?";


        try (Connection conexao = ConexaoBanco.conectar();
        PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idUsuario);

            try (ResultSet resultado = comando.executeQuery()){

                boolean existe = resultado.next();

                return existe;
            }
        }
    }
}