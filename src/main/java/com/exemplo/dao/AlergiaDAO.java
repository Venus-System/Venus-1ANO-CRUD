package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Alergia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela alergia: faz o CRUD das alergias no banco.
public class AlergiaDAO {

    // Cadastra uma nova alergia. Devolve true se o banco gravou a linha.
    public boolean cadastrar(Alergia alergia) throws SQLException {
        String sql= "insert into alergia (nome_alergia) values (?)";

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstm = cnn.prepareStatement(sql)){
             pstm.setString(1, alergia.getNomeAlergia());

             return pstm.executeUpdate()>0;
        }
    }

    // Lista todas as alergias, em ordem de id, para mostrar na tabela da tela.
    public ArrayList<Alergia> listar() throws SQLException{
            String sql= "select * from alergia order by id_alergia";
            ArrayList<Alergia> alergia = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()){
                Alergia al1 = new Alergia(rset.getInt("id_alergia"), rset.getString("nome_alergia"));
            alergia.add(al1);
            }
        } return alergia;
    }

    // Busca uma alergia pelo id. Usado para preencher o formulário de alteração.
    public Alergia listarPorId(int id) throws SQLException{
        String sql= "select*from alergia where id_alergia = ?";
        Alergia alergia = null;

        try (Connection cnn = ConexaoBanco.conectar();
        PreparedStatement pstm = cnn.prepareStatement(sql)){
            pstm.setInt(1,id);

            try (ResultSet rset = pstm.executeQuery()) {
                if(rset.next()){
                    alergia = new Alergia(
                            rset.getInt("id_alergia"),
                            rset.getString("nome_alergia"));
                }
            }
        }return alergia;

    }


    // Altera o nome da alergia escolhida. Devolve quantas linhas mudaram.
    public int alterar(Alergia alergia) throws SQLException{
        String sql = "update alergia set nome_alergia = ? where id_alergia = ?";

        try (Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setString(1, alergia.getNomeAlergia());
            pstm.setInt(2, alergia.getIdAlergia());

            return pstm.executeUpdate();
        }
    }

    // Deleta a alergia pelo id. Devolve quantas linhas foram apagadas.
    public int deletarPorId(int id) throws SQLException{
        String sql = "delete from alergia where id_alergia = ?";

        try (Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstm = cnn.prepareStatement(sql)){

            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }



}
