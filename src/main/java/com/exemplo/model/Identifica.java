package com.exemplo.model;

public class Identifica {
    private int idIngrediente;
    private int idAnalise;
    private String tipoIngrediente;
    private int nivelPerigo;

    public Identifica(int idIngrediente, int idAnalise) {
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
    }

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

    @Override
    public String toString() {
        return "Id Ingrediente: " + getIdIngrediente() + "\n" +
                "Id Análise: " + getIdAnalise() + "\n";
    }
}
