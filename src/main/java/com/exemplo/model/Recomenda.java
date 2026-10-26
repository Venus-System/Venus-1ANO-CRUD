package com.exemplo.model;

public class Recomenda {
    private int idProduto;
    private int idUsuario;
    // Dados do produto e do usuário preenchidos pelas consultas com junção no DAO.
    private String nomeProduto;
    private String marcaProduto;
    private String nomeUsuario;

    public Recomenda(int idProduto, int idUsuario) {
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getMarcaProduto() {
        return marcaProduto;
    }

    public void setMarcaProduto(String marcaProduto) {
        this.marcaProduto = marcaProduto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    @Override
    public String toString() {
        return "Id Produto: " + getIdProduto() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
