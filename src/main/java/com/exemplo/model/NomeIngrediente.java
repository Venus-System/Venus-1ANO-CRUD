package com.exemplo.model;

// Model da tabela nome_ingrediente: cada objeto desta classe representa um dos nomes de um ingrediente, porque o mesmo ingrediente pode ter vários nomes.
public class NomeIngrediente {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idNomeIngrediente;
    private String nomeIngrediente;
    private int idIngrediente;

    // Construtor completo: usado quando os dados são lidos do banco.
    public NomeIngrediente(String nomeIngrediente, int idIngrediente, int idNomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
        this.idIngrediente = idIngrediente;
        this.idNomeIngrediente = idNomeIngrediente;
    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public NomeIngrediente(String nomeIngrediente, int idIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
        this.idIngrediente = idIngrediente;
    }

    // Construtor com o id e os campos que podem mudar: usado na alteração.
    public NomeIngrediente(int idNomeIngrediente, String nomeIngrediente) {
        this.idNomeIngrediente = idNomeIngrediente;
        this.nomeIngrediente = nomeIngrediente;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public void setNomeIngrediente(String nomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public int getIdNomeIngrediente() {
        return idNomeIngrediente;
    }

    public void setIdNomeIngrediente(int idNomeIngrediente) {
        this.idNomeIngrediente = idNomeIngrediente;
    }

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Nome Ingrediente: " + getIdNomeIngrediente()+ "\n" +
                "Nome Ingrediente: " + getNomeIngrediente()+ "\n" +
                "Id Ingrediente: " + getIdIngrediente() + "\n";
    }
}


