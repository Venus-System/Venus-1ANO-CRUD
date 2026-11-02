package com.exemplo.model;

// Model da tabela alergia: cada objeto desta classe representa um tipo de alergia que o usuário pode ter.
public class Alergia {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idAlergia;
    private String nomeAlergia;

    // Construtor completo: usado quando os dados são lidos do banco.
    public Alergia(int idAlergia, String nomeAlergia) {
        this.idAlergia = idAlergia;
        this.nomeAlergia = nomeAlergia;
    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public Alergia(String nomeAlergia) {
        this.nomeAlergia = nomeAlergia;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
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

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "id Alergia: " + getIdAlergia() + "\n"+
                "Nome Alergia: " + getNomeAlergia() + "\n";
    }
}
