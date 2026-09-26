package com.exemplo.model;

public class IngredienteAnalise {
    private int idIngredienteAnalise;
    private int idIngrediente;
    private int idAnalise;

    public IngredienteAnalise(int idIngredienteAnalise, int idIngrediente, int idAnalise) {
        this.idIngredienteAnalise = idIngredienteAnalise;
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
    }

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

    @Override
    public String toString() {
        return "Id IngredienteAnálise: " + getIdIngredienteAnalise() + "\n" +
                "Id Ingrediente: " + getIdIngrediente() + "\n";
    }
}