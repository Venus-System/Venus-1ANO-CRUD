package com.exemplo.model;

public class NomeIngrediente {

    private int idNomeIngrediente;
    private String nomeIngrediente;
    private int idIngrediente;

    public NomeIngrediente(String nomeIngrediente, int idIngrediente, int idNomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
        this.idIngrediente = idIngrediente;
        this.nomeIngrediente = nomeIngrediente;
    }

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

    @Override
    public String toString() {
        return "Id Nome Ingrediente: " + getNomeIngrediente()+ "\n" +
                "Nome Ingrediente: " + getNomeIngrediente()+ "\n" +
                "Id Ingrediente: " + getIdIngrediente() + "\n";
    }
}


