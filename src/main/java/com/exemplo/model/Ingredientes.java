package com.exemplo.model;

import java.time.LocalDate;

// Model da tabela ingredientes: cada objeto desta classe representa um ingrediente, com o tipo e o nível de perigo dele.
public class Ingredientes {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idIngrediente;
    private int nivelPerigo;
    private String tipo;
    private LocalDate dtAtualizacao;

    // Construtor completo: usado quando os dados são lidos do banco.
    public Ingredientes(int idIngrediente, int nivelPerigo, String tipo, LocalDate dtAtualizacao) {
        this.idIngrediente = idIngrediente;
        this.nivelPerigo = nivelPerigo;
        this.tipo = tipo;
        this.dtAtualizacao = dtAtualizacao;
    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public Ingredientes(int nivelPerigo, String tipo) {
        this.nivelPerigo = nivelPerigo;
        this.tipo = tipo;
    }

    // Construtor com o id e os campos que podem mudar: usado na alteração.
    public Ingredientes(int idIngrediente, int nivelPerigo, String tipo) {
        this.idIngrediente = idIngrediente;
        this.nivelPerigo = nivelPerigo;
        this.tipo = tipo;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
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

    public LocalDate getDtAtualizacao() {
        return dtAtualizacao;
    }

    public void setDtAtualizacao(LocalDate dtAtualizacao) {
        this.dtAtualizacao = dtAtualizacao;
    }

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Ingrediente: " + getIdIngrediente() + "\n" +
                "Nível Perigo: " + getNivelPerigo() + "\n" +
                "Tipo: " + getTipo() + "\n" +
                "Data Atualização: " + getDtAtualizacao() + "\n";
    }
}
