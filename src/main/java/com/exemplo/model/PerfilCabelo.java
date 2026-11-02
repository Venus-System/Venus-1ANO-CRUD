package com.exemplo.model;

// Model da tabela perfil_cabelo: cada objeto desta classe representa o perfil de cabelo do usuário (curvatura, oleosidade e espessura).
public class PerfilCabelo {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idPerfilCabelo;
    private int curvatura;
    private int oleosidade;
    private int espessura;
    private int idUsuario;

    // Construtor completo: usado quando os dados são lidos do banco.
    public PerfilCabelo(int curvatura, int oleosidade, int espessura, int idUsuario, int idPerfilCabelo) {
        this.curvatura = curvatura;
        this.oleosidade = oleosidade;
        this.espessura = espessura;
        this.idUsuario = idUsuario;
        this.idPerfilCabelo = idPerfilCabelo;
    }

    // Construtor com o id e os campos que podem mudar: usado na alteração.
    public PerfilCabelo(int idPerfilCabelo, int curvatura, int oleosidade, int espessura) {
        this.idPerfilCabelo = idPerfilCabelo;
        this.curvatura = curvatura;
        this.oleosidade = oleosidade;
        this.espessura = espessura;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
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

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Perfil Cabelo: " + getIdPerfilCabelo() + "\n" +
                "Curvatura: " + getCurvatura() + "\n" +
                "Oleosidade: " + getOleosidade() + "\n" +
                "Espessura: " + getEspessura() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
