//recomendação.
package com.exemplo.model;

public class ProdutoUsuario {

    private int idProdutoUsuario;
    private int idProduto;
    private int idUsuario;

    public ProdutoUsuario(int idProdutoUsuario, int idProduto, int idUsuario) {
        this.idProdutoUsuario = idProdutoUsuario;
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
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

    @Override
    public String toString() {
        return "Id Produto Usuário: " + getIdProdutoUsuario() + "\n" +
                "Id Produto: " + getIdProduto() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
