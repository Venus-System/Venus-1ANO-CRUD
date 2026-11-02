package com.exemplo.model;

// Model da tabela identifica: cada objeto desta classe representa a ligação entre uma análise e os ingredientes identificados nela.
public class Identifica {
    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idIngrediente;
    private int idAnalise;
    // Estes atributos vêm de outras tabelas pelo JOIN e servem só para mostrar os dados na tela.
    private String tipoIngrediente;
    private int nivelPerigo;

    // Construtor: recebe os dois ids que ligam as tabelas.
    public Identifica(int idIngrediente, int idAnalise) {
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public int getIdAnalise() {
        return idAnalise;
    }

    public void setIdAnalise(int idAnalise) {
        this.idAnalise = idAnalise;
    }

    public String getTipoIngrediente() {
        return tipoIngrediente;
    }

    public void setTipoIngrediente(String tipoIngrediente) {
        this.tipoIngrediente = tipoIngrediente;
    }

    public int getNivelPerigo() {
        return nivelPerigo;
    }

    public void setNivelPerigo(int nivelPerigo) {
        this.nivelPerigo = nivelPerigo;
    }

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Ingrediente: " + getIdIngrediente() + "\n" +
                "Id Análise: " + getIdAnalise() + "\n";
    }
}
