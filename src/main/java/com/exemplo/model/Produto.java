package com.exemplo.model;

// Model da tabela produto: cada objeto desta classe representa um produto cadastrado no sistema.
public class Produto {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idProduto;
    private String nome;
    private String marca;
    private String categoria;
    private String descricao;
    private Boolean ehVegano;
    private Boolean ehCrueltyFree;
    private int pontuacao;
    private String listaIngredientes;

    // Construtor completo: usado quando os dados são lidos do banco.
    public Produto(int idProduto, String nome, String marca, String categoria, String descricao, Boolean ehVegano, Boolean ehCrueltyFree, int pontuacao, String listaIngredientes) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.marca = marca;
        this.categoria = categoria;
        this.descricao = descricao;
        this.ehVegano = ehVegano;
        this.ehCrueltyFree = ehCrueltyFree;
        this.pontuacao = pontuacao;
        this.listaIngredientes = listaIngredientes;
    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public Produto(String nome, String marca, String categoria, String descricao, Boolean ehVegano, Boolean ehCrueltyFree, int pontuacao, String listaIngredientes) {
        this.nome = nome;
        this.marca = marca;
        this.categoria = categoria;
        this.descricao = descricao;
        this.ehVegano = ehVegano;
        this.ehCrueltyFree = ehCrueltyFree;
        this.pontuacao = pontuacao;
        this.listaIngredientes = listaIngredientes;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getEhVegano() {
        return ehVegano;
    }

    public void setEhVegano(Boolean ehVegano) {
        this.ehVegano = ehVegano;
    }

    public Boolean getEhCrueltyFree() {
        return ehCrueltyFree;
    }

    public void setEhCrueltyFree(Boolean ehCrueltyFree) {
        this.ehCrueltyFree = ehCrueltyFree;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(String listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Produto: " + getIdProduto() + "\n" +
                "Nome Produto: " + getNome() + "\n" +
                "Marca: " + getMarca() + "\n" +
                "Categoria: " + getCategoria() + "\n" +
                "Descrição: " + getDescricao() + "\n" +
                "É Vegano: " + getEhVegano() + "\n" +
                "É Cruelty Free:" + getEhCrueltyFree() + "\n" +
                "Pontuação:" + getPontuacao() + "\n" +
                "Lista Ingredientes: " + getListaIngredientes() + "\n";
    }
}
