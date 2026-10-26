package com.exemplo.model;

public class ProdutoIngrediente {

    private int idProdutoIngrediente;
    private int idIngrediente;
    private int idProduto;
    // Dados do ingrediente preenchidos pelas consultas com junção no DAO.
    private String nomeIngrediente;
    private int nivelPerigo;
    private String tipo;


    public ProdutoIngrediente(int idProdutoIngrediente, int idIngrediente, int idProduto) {
        this.idProdutoIngrediente = idProdutoIngrediente;
        this.idIngrediente = idIngrediente;
        this.idProduto = idProduto;
    }


    public ProdutoIngrediente(int idIngrediente, int idProduto) {
        this.idIngrediente = idIngrediente;
        this.idProduto = idProduto;
    }

    public ProdutoIngrediente(int idProdutoIngrediente, int idProduto, int idIngrediente,
                              String nomeIngrediente, int nivelPerigo, String tipo) {
        this.idProdutoIngrediente = idProdutoIngrediente;
        this.idProduto = idProduto;
        this.idIngrediente = idIngrediente;
        this.nomeIngrediente = nomeIngrediente;
        this.nivelPerigo = nivelPerigo;
        this.tipo = tipo;
    }

    public int getIdProdutoIngrediente() {
        return idProdutoIngrediente;
    }

    public void setIdProdutoIngrediente(int idProdutoIngrediente) {
        this.idProdutoIngrediente = idProdutoIngrediente;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public void setNomeIngrediente(String nomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
    }

    public int getNivelPerigo() {
        return nivelPerigo;
    }

    public void setNivelPerigo(int nivelPerigo) {
        this.nivelPerigo = nivelPerigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Id Produto Ingrediente: " + getIdProdutoIngrediente() + "\n" +
                "Id Ingrediente: " + getIdIngrediente() + "\n" +
                "Id Produto: " + getIdProduto() + "\n";
    }
}
