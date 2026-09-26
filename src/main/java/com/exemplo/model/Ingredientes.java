package com.exemplo.model;

public class Ingredientes {

    private int idIngrediente;
    private int nivelPerigo;
    private String tipo;
    private String dtAtualizacao;

    public Ingredientes(int idIngrediente, int nivelPerigo, String tipo, String dtAtualizacao) {
        this.idIngrediente = idIngrediente;
        this.nivelPerigo = nivelPerigo;
        this.tipo = tipo;
        this.dtAtualizacao = dtAtualizacao;
    }

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

    public String getDtAtualizacao() {
        return dtAtualizacao;
    }

    public void setDtAtualizacao(String dtAtualizacao) {
        this.dtAtualizacao = dtAtualizacao;
    }

    @Override
    public String toString() {
        return "Id Ingrediente: " + getIdIngrediente() + "\n" +
                "Nível Perigo: " + getNivelPerigo() + "\n" +
                "Tipo: " + getTipo() + "\n" +
                "Data Atualização: " + getDtAtualizacao() + "\n";
    }
}
