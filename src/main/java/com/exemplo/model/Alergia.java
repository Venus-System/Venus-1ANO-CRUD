package com.exemplo.model;

public class Alergia {

    private int idAlergia;
    private String nomeAlergia;

    public Alergia(int idAlergia, String nomeAlergia) {
        this.idAlergia = idAlergia;
        this.nomeAlergia = nomeAlergia;
    }

    public int getIdAlergia() {
        return idAlergia;
    }

    public void setIdAlergia(int idAlergia) {
        this.idAlergia = idAlergia;
    }

    public String getNomeAlergia() {
        return nomeAlergia;
    }

    public void setNomeAlergia(String nomeAlergia) {
        this.nomeAlergia = nomeAlergia;
    }

    @Override
    public String toString() {
        return "id Alergia: " + getIdAlergia() +
                "Nome Alergia: " + getNomeAlergia() + "\n";
    }
}
