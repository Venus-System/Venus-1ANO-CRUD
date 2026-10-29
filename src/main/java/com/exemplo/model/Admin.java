package com.exemplo.model;

import java.time.LocalDate;

public class Admin {

    private int idAdmin;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dtCadastro;

    public Admin(int idAdmin, String nome, String email, String senha, LocalDate dtCadastro) {
        this.idAdmin = idAdmin;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dtCadastro = dtCadastro;
    }

    public Admin(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    @Override
    public String toString() {
        return "id Admin: " + getIdAdmin() + "\n" +
                "Nome: " + getNome() + "\n" +
                "Email: " + getEmail() + "\n";
    }
}
