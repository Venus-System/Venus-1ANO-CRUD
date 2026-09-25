package com.exemplo.model;

public class ProdutoIngrediente {

    private int idProdutoIngrediente;
    private int idIngrediente;
    private int idProduto;

    public ProdutoIngrediente(int idProdutoIngrediente, int idIngrediente, int idProduto) {
        this.idProdutoIngrediente = idProdutoIngrediente;
        this.idIngrediente = idIngrediente;
        this.idProduto = idProduto;
    }

    public int getIdProdutoIngrediente() {
        return idProdutoIngrediente;
    }

    public void setIdProdutoIngrediente(int idProdutoIngrediente) {
        this.idProdutoIngrediente = idProdutoIngrediente;
    }

    public int getidIngrediente() {
        return idIngrediente;
    }

    public void setiIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    @Override
    public String toString() {
        return "Id Produto Ingrediente: " + getIdProdutoIngrediente() + "\n" +
                "Id Ingrediente: " + getidIngrediente() + "\n" +
                "Id Produto: " + getIdProduto() + "\n";
    }
}
