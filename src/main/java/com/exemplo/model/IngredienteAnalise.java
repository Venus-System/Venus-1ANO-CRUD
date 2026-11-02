package com.exemplo.model;

// Model da tabela ingrediente_analise: cada objeto desta classe representa um ingrediente encontrado em uma análise.
public class IngredienteAnalise {
    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idIngredienteAnalise;
    private int idIngrediente;
    private int idAnalise;
    // Estes atributos vêm de outras tabelas pelo JOIN e servem só para mostrar os dados na tela.
    private String tipo;
    private int nivelPerigo;

    // Construtor completo: usado quando os dados são lidos do banco.
    public IngredienteAnalise(int idIngredienteAnalise, int idIngrediente, int idAnalise) {
        this.idIngredienteAnalise = idIngredienteAnalise;
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public IngredienteAnalise(int idIngrediente, int idAnalise) {
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
    }

    // Construtor com os dados do JOIN: usado nas listagens que mostram nomes e detalhes.
    public IngredienteAnalise(int idIngredienteAnalise, int idIngrediente, int idAnalise,
                              String tipo, int nivelPerigo) {
        this.idIngredienteAnalise = idIngredienteAnalise;
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
        this.tipo = tipo;
        this.nivelPerigo = nivelPerigo;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
    public int getIdAnalise() {
        return idAnalise;
    }

    public void setIdAnalise(int idAnalise) {
        this.idAnalise = idAnalise;
    }

    public int getIdIngredienteAnalise() {
        return idIngredienteAnalise;
    }

    public void setIdIngredienteAnalise(int idIngredienteAnalise) {
        this.idIngredienteAnalise = idIngredienteAnalise;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        return "Id IngredienteAnálise: " + getIdIngredienteAnalise() + "\n" +
                "Id Ingrediente: " + getIdIngrediente() + "\n";
    }
}