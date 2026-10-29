package com.exemplo.dao;

import com.exemplo.model.Admin;
import com.exemplo.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDAO {

    public boolean inserirAdmin(Admin admin) throws SQLException {
        String sql = "insert into admin (nome, email, senha) values (?, ?, ?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setString(1, admin.getNome());
            pstm.setString(2, admin.getEmail());
            pstm.setString(3, admin.getSenha());

            return pstm.executeUpdate() > 0;
        }
    }

    public ArrayList<Admin> read() throws SQLException {
        String sql = "select * from admin order by id_admin";
        ArrayList<Admin> admins = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {
            while (rset.next()) {
                admins.add(montar(rset));
            }
        }
        return admins;
    }

    public Admin readById(int id) throws SQLException {
        String sql = "select * from admin where id_admin = ?";
        Admin admin = null;

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    admin = montar(rset);
                }
            }
        }
        return admin;
    }

    public int update(Admin admin) throws SQLException {
        String sql = "update admin set nome = ?, email = ? where id_admin = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setString(1, admin.getNome());
            pstm.setString(2, admin.getEmail());
            pstm.setInt(3, admin.getIdAdmin());

            return pstm.executeUpdate();
        }
    }

    public int trocarSenha(int id, String senha) throws SQLException {
        String sql = "update admin set senha = ? where id_admin = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setString(1, senha);
            pstm.setInt(2, id);

            return pstm.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from admin where id_admin = ?";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)) {
            pstm.setInt(1, id);

            return pstm.executeUpdate();
        }
    }

    private Admin montar(ResultSet rset) throws SQLException {
        Date data = rset.getDate("dt_cadastro");
        return new Admin(
                rset.getInt("id_admin"),
                rset.getString("nome"),
                rset.getString("email"),
                rset.getString("senha"),
                data == null ? null : data.toLocalDate());
    }
}
