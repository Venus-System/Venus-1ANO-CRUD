package com.exemplo.model;

public class IngredienteAnalise {
    private int idIngredienteAnalise;
    private int idIngrediente;
    private int idAnalise;
    // Dados do ingrediente preenchidos pelas consultas com junção no DAO.
    private String tipo;
    private int nivelPerigo;

    public IngredienteAnalise(int idIngredienteAnalise, int idIngrediente, int idAnalise) {
        this.idIngredienteAnalise = idIngredienteAnalise;
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
    }

    public IngredienteAnalise(int idIngrediente, int idAnalise) {
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
    }

    public IngredienteAnalise(int idIngredienteAnalise, int idIngrediente, int idAnalise,
                              String tipo, int nivelPerigo) {
        this.idIngredienteAnalise = idIngredienteAnalise;
        this.idIngrediente = idIngrediente;
        this.idAnalise = idAnalise;
        this.tipo = tipo;
        this.nivelPerigo = nivelPerigo;
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

    @Override
    public String toString() {
        return "Id IngredienteAnálise: " + getIdIngredienteAnalise() + "\n" +
                "Id Ingrediente: " + getIdIngrediente() + "\n";
    }
}