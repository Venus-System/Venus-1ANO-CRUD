package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.UsuarioAlergia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;


// DAO da tabela usuario_alergia: liga os usuários às alergias que eles têm.
public class UsuarioAlergiaDAO {
    // Cadastra uma alergia para um usuário, com o grau.
    public boolean cadastrar(UsuarioAlergia usuarioAlergia) throws SQLException {
        String sql= "insert into usuario_alergia (grau, id_usuario , id_alergia) values (?,?,?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
                pstm.setInt(1,usuarioAlergia.getGrau());
                pstm.setInt(2,usuarioAlergia.getIdUsuario());
                pstm.setInt(3,usuarioAlergia.getIdAlergia());
            return pstm.executeUpdate()>0;
        }
    }

    // Lista todas as alergias dos usuários, em ordem de id.
    public ArrayList<UsuarioAlergia> listar() throws SQLException{
        String sql= "select*from usuario_alergia order by id_usuario_alergia";
        ArrayList<UsuarioAlergia> usuarioAlergia = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()){
                UsuarioAlergia usAl = new UsuarioAlergia (
                        rset.getInt("id_usuario_alergia"),
                        rset.getObject("dt_registro", LocalDate.class),
                        rset.getInt("grau"),
                        rset.getInt("id_usuario"),
                        rset.getInt("id_alergia"));
                usuarioAlergia.add(usAl);
            }
        } return usuarioAlergia;
    }

    // Busca um registro pelo id. Usado para preencher o formulário de alteração.
    public UsuarioAlergia listarPorId(int id) throws SQLException{
        String sql= "select*from usuario_alergia where id_usuario_alergia = ?";
        UsuarioAlergia usuarioAlergia = null;

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1,id);

            try (ResultSet rset = pstm.executeQuery()) {
                if(rset.next()){
                    usuarioAlergia  = new UsuarioAlergia(
                            rset.getInt("id_usuario_alergia"),
                            rset.getObject("dt_registro", LocalDate.class),
                            rset.getInt("grau"),
                            rset.getInt("id_usuario"),
                            rset.getInt("id_alergia"));
                }
            }
        }return usuarioAlergia;

    }

    // Lista as alergias do usuário escolhido. O JOIN com alergia traz o nome de cada alergia.
    public ArrayList<UsuarioAlergia> listarPorIdUsuario(int id) throws SQLException{
        String sql= "select ua.*, a.nome_alergia " +
                "from usuario_alergia ua " +
                "join alergia a on a.id_alergia = ua.id_alergia " +
                "where ua.id_usuario = ? " +
                "order by a.nome_alergia";
        ArrayList<UsuarioAlergia> usuarioAlergia= new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1,id);

            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()){
                    UsuarioAlergia usAl1 = new UsuarioAlergia(
                            rset.getInt("id_usuario_alergia"),
                            rset.getObject("dt_registro", LocalDate.class),
                            rset.getInt("grau"),
                            rset.getInt("id_usuario"),
                            rset.getInt("id_alergia"));
                    usAl1.setNomeAlergia(rset.getString("nome_alergia"));
                    usuarioAlergia.add(usAl1);
                }
            }
        }return usuarioAlergia;

    }

    // Altera o grau da alergia do usuário.
    public int alterar(UsuarioAlergia usuarioAlergia) throws SQLException{
        String sql = "update usuario_alergia set grau =? where id_usuario_alergia=?";
        try (Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstmt = cnn.prepareStatement(sql)){

            pstmt.setInt(1, usuarioAlergia.getGrau());
            pstmt.setInt(2, usuarioAlergia.getIdUsuarioAlergia());

            return pstmt.executeUpdate();

        }
    }

    // Deleta o registro pelo id.
    public int deletarPorId(int id) throws SQLException{
        String sql = "delete from usuario_alergia where id_usuario_alergia = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    // Deleta a alergia escolhida de todos os usuários.
    public int deletarPorIdAlergia(int id) throws SQLException{
        String sql = "delete from usuario_alergia where id_alergia = ?";

        try (Connection cnn = ConexaoBanco.conectar();
           PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    // Deleta todas as alergias do usuário escolhido.
    public int deletarPorIdUsuario(int id) throws SQLException{
        String sql = "delete from usuario_alergia where id_usuario = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

}
