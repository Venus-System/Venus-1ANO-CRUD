package com.exemplo.model;

// Model da tabela perfil_pele: cada objeto desta classe representa o perfil de pele do usuário (tipo, oleosidade e sensibilidade).
public class PerfilPele {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idPerfilPele;
    private int sensibilidade;
    private String tipoPele;
    private int nivelOleosidade;
    private int idUsuario;

    // Construtor completo: usado quando os dados são lidos do banco.
    public PerfilPele(int sensibilidade, String tipoPele, int nivelOleosidade, int idUsuario, int idPerfilPele) {
        this.sensibilidade = sensibilidade;
        this.tipoPele = tipoPele;
        this.nivelOleosidade = nivelOleosidade;
        this.idUsuario = idUsuario;
        this.idPerfilPele = idPerfilPele;
    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public PerfilPele(int sensibilidade, String tipoPele, int nivelOleosidade, int idUsuario) {
        this.sensibilidade = sensibilidade;
        this.tipoPele = tipoPele;
        this.nivelOleosidade = nivelOleosidade;
        this.idUsuario = idUsuario;
    }

    // Construtor com o id e os campos que podem mudar: usado na alteração.
    public PerfilPele(int idPerfilPele, int sensibilidade, String tipoPele, int nivelOleosidade) {
        this.idPerfilPele = idPerfilPele;
        this.sensibilidade = sensibilidade;
        this.tipoPele = tipoPele;
        this.nivelOleosidade = nivelOleosidade;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
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

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Perfil Pele: " + getIdPerfilPele() + "\n" +
                "Sensibilidade: " + getSensibilidade() + "\n" +
                "Tipo Pele: " + getTipoPele() + "\n" +
                "Nível Oleosidade: " + getNivelOleosidade() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
