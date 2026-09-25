package com.exemplo.model;

public class PerfilCabelo {

    private int idPerfilCabelo;
    private int curvatura;
    private int oleosidade;
    private int espessura;
    private int idUsuario;

    public PerfilCabelo(int curvatura, int oleosidade, int espessura, int idUsuario, int idPerfilCabelo) {
        this.curvatura = curvatura;
        this.oleosidade = oleosidade;
        this.espessura = espessura;
        this.idUsuario = idUsuario;
        this.idPerfilCabelo = idPerfilCabelo;
    }

    public int getCurvatura() {
        return curvatura;
    }

    public void setCurvatura(int curvatura) {
        this.curvatura = curvatura;
    }

    public int getOleosidade() {
        return oleosidade;
    }

    public void setOleosidade(int oleosidade) {
        this.oleosidade = oleosidade;
    }

    public int getEspessura() {
        return espessura;
    }

    public void setEspessura(int espessura) {
        this.espessura = espessura;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPerfilCabelo() {
        return idPerfilCabelo;
    }

    public void setIdPerfilCabelo(int idPerfilCabelo) {
        this.idPerfilCabelo = idPerfilCabelo;
    }

    @Override
    public String toString() {
        return "Id Perfil Cabelo: " + getIdPerfilCabelo() + "\n" +
                "Curvatura: " + getCurvatura() + "\n" +
                "Oleosidade: " + getOleosidade() + "\n" +
                "Espessura: " + getEspessura() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
