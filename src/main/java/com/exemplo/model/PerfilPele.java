package com.exemplo.model;

public class PerfilPele {

    private int idPerfilPele;
    private int sensibilidade;
    private String tipoPele;
    private int nivelOleosidade;
    private int idUsuario;

    public PerfilPele(int sensibilidade, String tipoPele, int nivelOleosidade, int idUsuario, int idPerfilPele) {
        this.sensibilidade = sensibilidade;
        this.tipoPele = tipoPele;
        this.nivelOleosidade = nivelOleosidade;
        this.idUsuario = idUsuario;
        this.idPerfilPele = idPerfilPele;
    }

    public int getSensibilidade() {
        return sensibilidade;
    }

    public void setSensibilidade(int sensibilidade) {
        this.sensibilidade = sensibilidade;
    }

    public String getTipoPele() {
        return tipoPele;
    }

    public void setTipoPele(String tipoPele) {
        this.tipoPele = tipoPele;
    }

    public int getNivelOleosidade() {
        return nivelOleosidade;
    }

    public void setNivelOleosidade(int nivelOleosidade) {
        this.nivelOleosidade = nivelOleosidade;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPerfilPele() {
        return idPerfilPele;
    }

    public void setIdPerfilPele(int idPerfilPele) {
        this.idPerfilPele = idPerfilPele;
    }

    @Override
    public String toString() {
        return "Id Perfil Pele: " + getIdPerfilPele() + "\n" +
                "Sensibilidade: " + getSensibilidade() + "\n" +
                "Tipo Pele: " + getTipoPele() + "\n" +
                "Nível Oleosidade: " + getNivelOleosidade() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
