//recomendação.
package com.exemplo.model;

public class ProdutoUsuario {

    private int idProdutoUsuario;
    private int idProduto;
    private int idUsuario;
    private String nomeProduto;
    private String nomeUsuario;

    public ProdutoUsuario(int idProdutoUsuario, int idProduto, int idUsuario) {
        this.idProdutoUsuario = idProdutoUsuario;
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
    }

    public ProdutoUsuario(int idProduto, int idUsuario) {
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
    }

    public ProdutoUsuario(int idProdutoUsuario, int idProduto, int idUsuario, String nomeProduto, String nomeUsuario) {
        this.idProdutoUsuario = idProdutoUsuario;
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
        this.nomeProduto = nomeProduto;
        this.nomeUsuario = nomeUsuario;
    }

    public int getIdProdutoUsuario() {
        return idProdutoUsuario;
    }

    public void setIdProdutoUsuario(int idProdutoUsuario) {
        this.idProdutoUsuario = idProdutoUsuario;
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

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    @Override
    public String toString() {
        return "Id Produto Usuário: " + getIdProdutoUsuario() + "\n" +
                "Id Produto: " + getIdProduto() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
