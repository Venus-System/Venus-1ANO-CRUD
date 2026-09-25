package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Alergia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AlergiaDAO {

    public boolean inserirAlergia(Alergia alergia) throws SQLException {
        String sql= "insert into alergia (nome_alergia) values (?)";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
             pstm.setString(1, alergia.getNomeAlergia());

             return pstm.executeUpdate()>0;
        }
    }

    public ArrayList<Alergia> read() throws SQLException{
            String sql= "select*from alergia order by id_alergia";
            ArrayList<Alergia> alergia = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()){
                Alergia al1 = new Alergia(rset.getInt("id_alergia"), rset.getString("nome_alergia"));            }
        } return alergia;
    }

    public Alergia readById(int id) throws SQLException{
        String sql= "select*from alergia where id_alergia = ?";
        Alergia alergia = null;

        try (Connection cnn = new ConexaoBanco().conectar();
        PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1,id);

            try (ResultSet rset = pstm.executeQuery()) {
                if(rset.next()){
                    alergia = new Alergia(rset.getInt("id_alergia"), rset.getString("nome_alergia"));
                }
            }
        }return alergia;

    }

    public Alergia readByName(String nome) throws SQLException{
        String sql= "select*from alergia where nome_alergia like ?";
        Alergia alergia = null;

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setString(1,"%"+nome+"%");

            try (ResultSet rset = pstm.executeQuery()) {
                if(rset.next()){
                    alergia = new Alergia(rset.getInt("id_alergia"), rset.getString("nome_alergia"));
                }
            }
        }return alergia;

    }

    public int update(Alergia alergia) throws SQLException{
        String sql = "update alergia set nome_alergia = ? where id_alergia = ?";

        try (Connection cnn = new ConexaoBanco().conectar();
            PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setString(1, alergia.getNomeAlergia());
            pstm.setInt(2, alergia.getIdAlergia());

            return pstm.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException{
        String sql = "delete from alergia where id_alergia = ?";

        try (Connection cnn = new ConexaoBanco().conectar();
            PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }

    public int deleteByNome(String nome) throws SQLException{
        String sql = "delete from alergia where nome_alergia= ?";

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setString(1,nome);
            return pstm.executeUpdate();
        }
    }


}
