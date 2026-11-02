package com.exemplo.dao;

import com.exemplo.util.ConexaoBanco;
import com.exemplo.model.Preferencias;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO da tabela preferencias: faz o CRUD das preferências dos usuários.
public class PreferenciasDAO {

    // Cadastra as preferências de um usuário.
    public boolean cadastrar(Preferencias preferencias) throws SQLException {
        String sql = "insert into preferencias (faixa_preco, prefere_vegano, restricoes_dieta, categorias_pref, marcas_fav, id_usuario) values (?, ?, ?, ?, ?, ?)";

        try (Connection cnn= ConexaoBanco.conectar();
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

    // Lista todas as preferências, em ordem de id.
    public ArrayList<Preferencias> listar() throws SQLException {
        String sql = "select * from preferencias order by id_preferencias";
        ArrayList<Preferencias> listaPreferencias = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
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
                listaPreferencias.add(prf1);
            }
        } return listaPreferencias;

    }

    // Busca uma preferência pelo id. Usado para preencher o formulário de alteração.
    public Preferencias listarPorId(int id) throws SQLException{
        String sql = "select * from preferencias where id_preferencias = ?";
        Preferencias preferencias = null;
        //ainda sem objeto
        try(Connection cnn = ConexaoBanco.conectar();
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

    // Lista as preferências do usuário escolhido.
    public ArrayList<Preferencias> listarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "select * from preferencias where id_usuario = ? order by id_preferencias";
        ArrayList<Preferencias> listaUsuario = new ArrayList<>();

        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    Preferencias pref1 = new Preferencias(
                            rset.getInt("id_preferencias"),
                            rset.getString("faixa_preco"),
                            rset.getBoolean("prefere_vegano"),
                            rset.getString("restricoes_dieta"),
                            rset.getString("categorias_pref"),
                            rset.getString("marcas_fav"),
                            rset.getInt("id_usuario"));
                    listaUsuario.add(pref1);
                }
            }
        }
        return listaUsuario;
    }

    // Lista as preferências que têm a faixa de preço pesquisada (filtro da tela).
    public ArrayList<Preferencias> listarPorPreco(String faixaPreco) throws SQLException{
        String sql = "select * from preferencias where faixa_preco like ?";
        ArrayList<Preferencias> listaPreferencias = new ArrayList<>();

        try(Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setString(1,"%"+faixaPreco+"%");

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                while (rset.next()){
                    Preferencias pref1 = new Preferencias(
                            rset.getInt("id_preferencias"),
                            rset.getString("faixa_preco"),
                            rset.getBoolean("prefere_vegano"),
                            rset.getString("restricoes_dieta"),
                            rset.getString("categorias_pref"),
                            rset.getString("marcas_fav"),
                            rset.getInt("id_usuario")

                            //retornará as preferencias com a faixa que está sendo procurada.
                    );
                    listaPreferencias.add(pref1);

                }
            }

        } return listaPreferencias;
    }

    // Lista as preferências que têm a marca pesquisada entre as favoritas (filtro da tela).
    public ArrayList<Preferencias> listarPorMarca(String marca) throws SQLException{
        String sql = "select * from preferencias where preferencias.marcas_fav like ?";
        ArrayList<Preferencias> listaPreferencias = new ArrayList<>();

        try(Connection cnn = ConexaoBanco.conectar();
            PreparedStatement pstmt= cnn.prepareStatement(sql)){

            pstmt.setString(1,"%"+marca+"%");

            try (ResultSet rset = pstmt.executeQuery()){
                //que permite a visualização das tabelas
                while (rset.next()){
                    Preferencias pref1 = new Preferencias(
                            rset.getInt("id_preferencias"),
                            rset.getString("faixa_preco"),
                            rset.getBoolean("prefere_vegano"),
                            rset.getString("restricoes_dieta"),
                            rset.getString("categorias_pref"),
                            rset.getString("marcas_fav"),
                            rset.getInt("id_usuario")

                            //retornará as preferencias com a marca que está sendo procurada.
                    );
                    listaPreferencias.add(pref1);
                }
            }

        } return listaPreferencias;
    }

    // Altera os dados da preferência escolhida.
    public int alterar(Preferencias preferencias) throws SQLException {
        String sql = "update preferencias set faixa_preco =?, prefere_vegano =?, restricoes_dieta =?, categorias_pref =?, marcas_fav =? where id_preferencias = ? ";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)){

            pstmt.setString(1, preferencias.getFaixaPreco());
            pstmt.setBoolean(2, preferencias.getPrefereVegano());
            pstmt.setString(3, preferencias.getRestricoesDieta());
            pstmt.setString(4, preferencias.getCategoriaPref());
            pstmt.setString(5, preferencias.getMarcasFav());
            pstmt.setInt(6, preferencias.getIdPreferencias());

            return pstmt.executeUpdate();
        }
    }

    // Deleta a preferência pelo id.
    public int deletarPorId(int id) throws SQLException {
        String sql = "delete from preferencias where id_preferencias = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            return pstmt.executeUpdate();

        }
    }

    // Deleta todas as preferências do usuário escolhido.
    public int deletarPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "delete from preferencias where id_usuario = ?";
        try (Connection cnn = ConexaoBanco.conectar();
             PreparedStatement pstmt = cnn.prepareStatement(sql)) {

            pstmt.setInt(1,idUsuario);
            return pstmt.executeUpdate();

        }
    }

}
