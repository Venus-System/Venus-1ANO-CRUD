package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Usuario;

import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO {

    public boolean cadastrarUsuario(Usuario usuario) throws SQLException {
        String sql = "insert into usuario(nome_completo, genero, email, senha ,telefone,  dt_nascimento ,dt_cadastro) values (?, ?, ?, ?, ?, ?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNomeCompleto());
            pstmt.setString(2, usuario.getGenero());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getSenha());
            pstmt.setString(5, usuario.getTelefone());
            pstmt.setObject(6, usuario.getDtNascimento());
            pstmt.setObject(7, usuario.getDtCadastro());
            // O cadastro retorna apenas se houve inserção, sem recuperar os dados gerados pelo banco.
            return pstmt.executeUpdate() > 0;
        }
    }


    public ArrayList<Usuario> read() throws SQLException {
        String sql = "select * from usuario order by id_usuario";
        ArrayList<Usuario> usuario = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                Usuario user1 = new Usuario(
                        rset.getInt("id_usuario"),
                        rset.getString("nome_completo"),
                        rset.getString("genero"),
                        rset.getString("email"),
                        rset.getString("senha"),
                        rset.getString("telefone"),
                        rset.getObject("dt_nascimento", LocalDate.class),
                        rset.getObject("dt_cadastro", LocalDate.class)
                );
                usuario.add(user1);
            }
        }
        return usuario;

    }

    public Usuario readById(int id) throws SQLException {
        String sql = "select * from usuario where id_usuario =?";
        Usuario usuario = null;

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    usuario = new Usuario(
                            rset.getInt("id_usuario"),
                            rset.getString("nome_completo"),
                            rset.getString("genero"),
                            rset.getString("email"),
                            rset.getString("senha"),
                            rset.getString("telefone"),
                            rset.getObject("dt_nascimento", LocalDate.class),
                            rset.getObject("dt_cadastro", LocalDate.class)

                    );
                }
            }

        }
        return usuario;
    }

    public Usuario readByEmail(String email) throws SQLException {
        String sql = "select * from usuario where email =?";
        Usuario usuario = null;
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    usuario = new Usuario(
                            rset.getInt("id_usuario"),
                            rset.getString("nome_completo"),
                            rset.getString("genero"),
                            rset.getString("email"),
                            rset.getString("senha"),
                            rset.getString("telefone"),
                            rset.getObject("dt_nascimento", LocalDate.class),
                            rset.getObject("dt_cadastro", LocalDate.class)

                    );
                }
            }

        }
        return usuario;
    }

    public ArrayList<Usuario> readByName(String nomeCompleto) throws SQLException {
        String sql = "select * from usuario where nome_completo like ? ";
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nomeCompleto + "%");

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    Usuario us1 = new Usuario(
                            rset.getInt("id_usuario"),
                            rset.getString("nome_completo"),
                            rset.getString("genero"),
                            rset.getString("email"),
                            rset.getString("senha"),
                            rset.getString("telefone"),
                            rset.getObject("dt_nascimento", LocalDate.class),
                            rset.getObject("dt_cadastro", LocalDate.class)

                    );

                    listaUsuarios.add(us1);
                }
            }

        }
        return listaUsuarios;
    }

    // Atualiza os dados pessoais sem alterar a senha nem a data de cadastro.
    public int update(Usuario usuario) throws SQLException {
        String sql = "update usuario set nome_completo = ?, genero = ?, email = ?, telefone = ?, dt_nascimento = ? where id_usuario = ? ";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNomeCompleto());
            pstmt.setString(2, usuario.getGenero());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getTelefone());
            pstmt.setObject(5, usuario.getDtNascimento());
            pstmt.setInt(6, usuario.getIdUsuario());

            return pstmt.executeUpdate();
        }
    }

    public boolean trocarSenha(int idUsuario, String novaSenha) throws SQLException {
        String sql = "update usuario set senha = ? where id_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, novaSenha);
            // A senha recebida deve estar em hash; este método apenas a grava.
            pstmt.setInt(2, idUsuario);

            int linhas = pstmt.executeUpdate();
            return linhas > 0;
        }
    }


    public int deleteById(int id) throws SQLException {
        String sql = "delete from usuario where id_usuario = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    public int deleteByEmail(String email) throws SQLException {
        String sql = "delete from usuario where email=?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            return pstmt.executeUpdate();
        }
    }

    public boolean existeEmail(String email) throws SQLException {
        String sql = "select 1 FROM usuario WHERE email = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, email);

            try (ResultSet resultado = comando.executeQuery()) {

                boolean existe = resultado.next();
                return existe;
            }
        }
    }

}


