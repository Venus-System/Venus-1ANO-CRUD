package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.UsuarioAlergia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;


public class UsuarioAlergiaDAO {
    public boolean inserirUsuarioAlergia(UsuarioAlergia usuarioAlergia) throws SQLException {
        String sql= "insert into usuario_alergia (dt_registro, grau, id_usuario , id_alergia) values (?,?,?,?)";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
                pstm.setObject(1, usuarioAlergia.getDtRegistro());
                pstm.setInt(2,usuarioAlergia.getGrau());
                pstm.setInt(3,usuarioAlergia.getIdUsuario());
                pstm.setInt(4,usuarioAlergia.getIdAlergia());
            return pstm.executeUpdate()>0;
        }
    }

    public ArrayList<UsuarioAlergia> read() throws SQLException{
        String sql= "select*from usuario_alergia order by id_usuario_alergia";
        ArrayList<UsuarioAlergia> usuarioAlergia = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
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

    public UsuarioAlergia readById(int id) throws SQLException{
        String sql= "select*from usuario_alergia where id_usuario_alergia = ?";
        UsuarioAlergia usuarioAlergia= null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1,id);

            try (ResultSet rset = pstm.executeQuery()) {
                if(rset.next()){
                    UsuarioAlergia usAl1 = new UsuarioAlergia(
                            rset.getInt("id_usuario_alergia"),
                            rset.getObject("dt_registro", LocalDate.class),
                            rset.getInt("grau"),
                            rset.getInt("id_usuario"),
                            rset.getInt("id_alergia"));
                }
            }
        }return usuarioAlergia;

    }

    public UsuarioAlergia readByIdUsuario(int id) throws SQLException{
        String sql= "select*from usuario_alergia where id_usuario = ?";
        UsuarioAlergia usuarioAlergia = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1,id);

            try (ResultSet rset = pstm.executeQuery()) {
                if(rset.next()){
                    UsuarioAlergia usAl1 = new UsuarioAlergia(
                            rset.getInt("id_usuario_alergia"),
                            rset.getObject("dt_registro", LocalDate.class),
                            rset.getInt("grau"),
                            rset.getInt("id_usuario"),
                            rset.getInt("id_alergia"));
                }
            }
        }return usuarioAlergia;

    }

    public int update (UsuarioAlergia usuarioAlergia) throws SQLException{
        String sql = "update usuario_alergia set dt_registro=?, grau =?, id_usuario=? , id_alergia =? where id_usuario_alergia=?";
        try (Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstmt = cnn.prepareStatement(sql)){

            pstmt.setObject(1, usuarioAlergia.getDtRegistro());
            pstmt.setInt(2, usuarioAlergia.getGrau());
            pstmt.setInt(3, usuarioAlergia.getIdUsuario());
            pstmt.setInt(4, usuarioAlergia.getIdAlergia());
            pstmt.setInt(5, usuarioAlergia.getIdUsuarioAlergia());

            return pstmt.executeUpdate();

        }
    }

    public int deleteById(int id) throws SQLException{
        String sql = "delete from usuario_alergia where id_usuario_alergia = ?";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    public int deleteByIdAlergia(int id) throws SQLException{
        String sql = "delete from usuario_alergia where id_alergia = ?";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    public int deleteByIdUsuario(int id) throws SQLException{
        String sql = "delete from usuario_alergia where id_usuario = ?";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

}
