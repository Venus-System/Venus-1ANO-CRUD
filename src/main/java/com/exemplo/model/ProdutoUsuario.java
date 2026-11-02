//recomendação.
package com.exemplo.model;

// Model da tabela produto_usuario: cada objeto desta classe representa a ligação entre um produto e um usuário.
public class ProdutoUsuario {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idProdutoUsuario;
    private int idProduto;
    private int idUsuario;
    // Estes atributos vêm de outras tabelas pelo JOIN e servem só para mostrar os dados na tela.
    private String nomeProduto;
    private String nomeUsuario;

    // Construtor completo: usado quando os dados são lidos do banco.
    public ProdutoUsuario(int idProdutoUsuario, int idProduto, int idUsuario) {
        this.idProdutoUsuario = idProdutoUsuario;
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public ProdutoUsuario(int idProduto, int idUsuario) {
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
    }

    // Construtor com os dados do JOIN: usado nas listagens que mostram nomes e detalhes.
    public ProdutoUsuario(int idProdutoUsuario, int idProduto, int idUsuario, String nomeProduto, String nomeUsuario) {
        this.idProdutoUsuario = idProdutoUsuario;
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
        this.nomeProduto = nomeProduto;
        this.nomeUsuario = nomeUsuario;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
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

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Produto Usuário: " + getIdProdutoUsuario() + "\n" +
                "Id Produto: " + getIdProduto() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
