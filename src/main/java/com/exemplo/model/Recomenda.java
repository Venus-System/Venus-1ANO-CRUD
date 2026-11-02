package com.exemplo.model;

// Model da tabela recomenda: cada objeto desta classe representa um produto recomendado para um usuário.
public class Recomenda {
    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idProduto;
    private int idUsuario;
    // Estes atributos vêm de outras tabelas pelo JOIN e servem só para mostrar os dados na tela.
    private String nomeProduto;
    private String marcaProduto;
    private String nomeUsuario;

    // Construtor: recebe os dois ids que ligam as tabelas.
    public Recomenda(int idProduto, int idUsuario) {
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
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

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Produto: " + getIdProduto() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
