package com.exemplo.model;

// Model da tabela produto_ingrediente: cada objeto desta classe representa a ligação entre um produto e os ingredientes que ele tem.
public class ProdutoIngrediente {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idProdutoIngrediente;
    private int idIngrediente;
    private int idProduto;
    // Estes atributos vêm de outras tabelas pelo JOIN e servem só para mostrar os dados na tela.
    private String nomeIngrediente;
    private int nivelPerigo;
    private String tipo;


    // Construtor completo: usado quando os dados são lidos do banco.
    public ProdutoIngrediente(int idProdutoIngrediente, int idIngrediente, int idProduto) {
        this.idProdutoIngrediente = idProdutoIngrediente;
        this.idIngrediente = idIngrediente;
        this.idProduto = idProduto;
    }


    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public ProdutoIngrediente(int idIngrediente, int idProduto) {
        this.idIngrediente = idIngrediente;
        this.idProduto = idProduto;
    }

    // Construtor com os dados do JOIN: usado nas listagens que mostram nomes e detalhes.
    public ProdutoIngrediente(int idProdutoIngrediente, int idProduto, int idIngrediente,
                              String nomeIngrediente, int nivelPerigo, String tipo) {
        this.idProdutoIngrediente = idProdutoIngrediente;
        this.idProduto = idProduto;
        this.idIngrediente = idIngrediente;
        this.nomeIngrediente = nomeIngrediente;
        this.nivelPerigo = nivelPerigo;
        this.tipo = tipo;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
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

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Produto Ingrediente: " + getIdProdutoIngrediente() + "\n" +
                "Id Ingrediente: " + getIdIngrediente() + "\n" +
                "Id Produto: " + getIdProduto() + "\n";
    }
}
