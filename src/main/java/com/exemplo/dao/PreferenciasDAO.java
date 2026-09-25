package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Preferencias;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PreferenciasDAO {

    public boolean cadastrarPreferencias(Preferencias preferencias) throws SQLException {
        String sql = "insert into preferencias (faixa_preco, prefere_vegano, restricoes_dieta, categorias_pref, marcas_fav, id_usuario) values (?, ?, ?, ?, ?, ?)";

        try (Connection cnn= new ConexaoBanco().conectar();
            PreparedStatement pstmt = cnn.prepareStatement(sql)){
                pstmt.setString(1, preferencias.getFaixaPreco());
                pstmt.setBoolean(2, preferencias.getPrefereVegano());
                pstmt.setString(3, preferencias.getRestricoesDieta());
                pstmt.setString(4, preferencias.getCategoriaPref());
                pstmt.setString(5, preferencias.getMarcasFav());
                pstmt.setInt(6, preferencias.getIdUsuario());

            return pstmt.executeUpdate()>0;
            // o executeUpdate so vai retornar quantas linhas do banco foram alteradas, não retorna os dados inseridos.
        }
    }

    public ArrayList<Preferencias> read() throws SQLException {
        String sql = "select * from preferencias order by id_preferencias";
        ArrayList<Preferencias> preferencias = new ArrayList<>();

        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {
            while (rset.next()) {
                Preferencias prf1 = new Preferencias(
                    rset.getInt("id_preferencias"),
                    rset.getString("faixa_preco"),
                    rset.getBoolean("prefere_vegano"),
                    rset.getString("restricoes_dieta"),
                    rset.getString("categorias_pref"),
                    rset.getString("marcas_fav"),
                    rset.getInt("id_usuario")

                );
                preferencias.add(prf1);
            }
        } return preferencias;

    }

    public Preferencias readById(int id) throws SQLException{
        String sql = "select * from preferencias where id_preferencias = ?";
        Preferencias preferencias = null;
        //ainda sem objeto
        try(Connection cnn = new ConexaoBanco().conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setInt(1,id);

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                if(rset.next()){
                    preferencias = new Preferencias(
                            rset.getInt("id_preferencias"),
                            rset.getString("faixa_preco"),
                            rset.getBoolean("prefere_vegano"),
                            rset.getString("restricoes_dieta"),
                            rset.getString("categorias_pref"),
                            rset.getString("marcas_fav"),
                            rset.getInt("id_usuario")

                            //retornará a preferencia com o id que está sendo procurado.
                    );
                }
            }

        } return preferencias;
    }

    public Preferencias readByPrice(String faixaPreco) throws SQLException{
        String sql = "select * from preferencias where faixa_preco like ?";
        Preferencias preferencias = null;
        //ainda sem objeto
        try(Connection cnn = new ConexaoBanco().conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setString(1,"%"+faixaPreco+"%");

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                if(rset.next()){
                    preferencias = new Preferencias(
                            rset.getInt("id_preferencias"),
                            rset.getString("faixa_preco"),
                            rset.getBoolean("prefere_vegano"),
                            rset.getString("restricoes_dieta"),
                            rset.getString("categorias_pref"),
                            rset.getString("marcas_fav"),
                            rset.getInt("id_usuario")

                            //retornará as preferencias com a faixa que está sendo procurada.
                    );
                }
            }

        } return preferencias;
    }

    public Preferencias readByBrand(String marca) throws SQLException{
        String sql = "select * from preferencias where preferencias.marcas_fav like ?";
        Preferencias preferencias = null;
        //ainda sem objeto
        try(Connection cnn = new ConexaoBanco().conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setString(1,"%"+marca+"%");

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                if(rset.next()){
                    preferencias = new Preferencias(
                            rset.getInt("id_preferencias"),
                            rset.getString("faixa_preco"),
                            rset.getBoolean("prefere_vegano"),
                            rset.getString("restricoes_dieta"),
                            rset.getString("categorias_pref"),
                            rset.getString("marcas_fav"),
                            rset.getInt("id_usuario")

                            //retornará as preferencias com a marca que está sendo procurada.
                    );
                }
            }

        } return preferencias;
    }

    public int update (Preferencias preferencias) throws SQLException {
        String sql = "update preferencias set faixa_preco =?, prefere_vegano =?, restricoes_dieta =?, categorias_pref =?, marcas_fav =?, id_usuario =? where id_preferencias = ? ";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)){

            pstmt.setString(1, preferencias.getFaixaPreco());
            pstmt.setBoolean(2, preferencias.getPrefereVegano());
            pstmt.setString(3, preferencias.getRestricoesDieta());
            pstmt.setString(4, preferencias.getCategoriaPref());
            pstmt.setString(5, preferencias.getMarcasFav());
            pstmt.setInt(6, preferencias.getIdUsuario());
            pstmt.setInt(7, preferencias.getIdPreferencias());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        String sql = "delete from preferencias where id_preferencias = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }

    public int deleteByIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from preferencias where id_usuario = ?";
        try (Connection cnn = new ConexaoBanco().conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,idUsuario);
            return pstmt.executeUpdate();

        }
    }
}
