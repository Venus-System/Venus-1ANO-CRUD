package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Usuario;

import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela usuario: faz o CRUD dos usuários e as consultas usadas no login.
public class UsuarioDAO {

    // Cadastra um novo usuário.
    public boolean cadastrar(Usuario usuario) throws SQLException {
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
            return pstmt.executeUpdate() > 0;
            // o executeUpdate so vai retornar quantas linhas do banco foram alteradas, não retorna os dados inseridos.
        }
    }


    // Lista todos os usuários, em ordem de id.
    public ArrayList<Usuario> listar() throws SQLException {
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

    // Busca um usuário pelo id. Usado para preencher o formulário de alteração e a tela de detalhes.
    public Usuario listarPorId(int id) throws SQLException {
        String sql = "select * from usuario where id_usuario =?";
        Usuario usuario = null; //ainda sem objeto

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                //que permite a visualização das tabelas
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

                            //retornará o usuario com o id que está sendo procurado.
                    );
                }
            }

        }
        return usuario;
    }

    // Confere se o usuário tem nível admin. Usado para liberar as telas de administrador.
    public boolean ehAdmin(int id) throws SQLException {
        String sql = "select 1 from usuario where id_usuario = ? and nivel = 'admin'";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                return rset.next();
            }
        }
    }

    // Busca o usuário pelo e-mail. Usado no login e na troca de senha.
    public Usuario listarPorEmail(String email) throws SQLException {
        String sql = "select * from usuario where email =? order by id_usuario limit 1";
        Usuario usuario = null;
        //ainda sem objeto
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rset = pstmt.executeQuery()) {
                //que permite a visualização das tabelas
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

                            //retornará o usuario com o email que está sendo procurado.
                    );
                    usuario.setNivel(rset.getString("nivel"));
                }
            }

        }
        return usuario;
    }

    // Lista os usuários cujo nome contém o texto pesquisado (filtro da tela).
    public ArrayList<Usuario> listarPorNome(String nomeCompleto) throws SQLException {
        String sql = "select * from usuario where nome_completo like ? ";
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        //ainda sem objeto
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nomeCompleto + "%");

            try (ResultSet rset = pstmt.executeQuery()) {
                //que permite a visualização das tabelas
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

                            //retornará os usuario com o nome que está sendo procurado.
                    );

                    listaUsuarios.add(us1);
                }
            }

        }
        return listaUsuarios;
    }

    // Altera os dados do usuário, menos a senha, que tem um método só para ela.
    public int alterar(Usuario usuario) throws SQLException {
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

    // Troca a senha do usuário.
    public boolean trocarSenha(int idUsuario, String novaSenha) throws SQLException {
        String sql = "update usuario set senha = ? where id_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setString(1, novaSenha);
            //o valor recebido deve ser o hash.
            pstmt.setInt(2, idUsuario);

            int linhas = pstmt.executeUpdate();
            return linhas > 0;
            //fica dentro do try porque é somente aqui dentro que linhas tem o valor que precisa devolver.
        }
    }


    // Deleta o usuário pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from usuario where id_usuario = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    // Deleta o usuário pelo e-mail.
    public int deletarPorEmail(String email) throws SQLException {
        String sql = "delete from usuario where email=?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            return pstmt.executeUpdate();
        }
    }

    // Confere se o e-mail já está cadastrado, para não deixar dois usuários com o mesmo e-mail.
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


