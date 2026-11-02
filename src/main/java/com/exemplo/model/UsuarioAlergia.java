package com.exemplo.model;

import java.time.LocalDate;

// Model da tabela usuario_alergia: cada objeto desta classe representa a ligação entre um usuário e uma alergia dele, com o grau.
public class UsuarioAlergia {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idUsuarioAlergia;
    private LocalDate dtRegistro;
    private int grau;
    private int idUsuario;
    private int idAlergia;
    // Este atributo vem da tabela alergia pelo JOIN e serve só para mostrar o nome na tela.
    private String nomeAlergia;

    // Construtor completo: usado quando os dados são lidos do banco.
    public UsuarioAlergia(int idUsuarioAlergia, LocalDate dtRegistro, int grau, int idUsuario, int idAlergia) {
        this.idUsuarioAlergia = idUsuarioAlergia;
        this.dtRegistro = dtRegistro;
        this.grau = grau;
        this.idUsuario = idUsuario;
        this.idAlergia = idAlergia;
    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public UsuarioAlergia(int grau, int idUsuario, int idAlergia) {
        this.grau = grau;
        this.idUsuario = idUsuario;
        this.idAlergia = idAlergia;
    }

    // Construtor com o id e os campos que podem mudar: usado na alteração.
    public UsuarioAlergia(int idUsuarioAlergia, int grau) {
        this.idUsuarioAlergia = idUsuarioAlergia;
        this.grau = grau;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
    public int getIdUsuarioAlergia() {
        return idUsuarioAlergia;
    }

    public void setIdUsuarioAlergia(int idUsuarioAlergia) {
        this.idUsuarioAlergia = idUsuarioAlergia;
    }

    public LocalDate getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(LocalDate dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public int getGrau() {
        return grau;
    }

    public void setGrau(int grau) {
        this.grau = grau;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Usuário Alergia: " + getIdUsuarioAlergia() + "\n" +
                "Data Registro: " + getDtRegistro() + "\n" +
                "Grau: " + getGrau() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n" +
                "Id Alergia: " + getIdAlergia() + "\n";
    }
}
