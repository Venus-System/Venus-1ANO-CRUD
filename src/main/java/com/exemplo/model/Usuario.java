package com.exemplo.model;

import java.time.LocalDate;

public class Usuario {

    private int idUsuario;
    private String nomeCompleto;
    private String genero;
    private String email;
    private String senha;
    private LocalDate dtNascimento;
    private String telefone;
    private LocalDate dtCadastro;

    public Usuario(int idUsuario, String nomeCompleto, String genero, String email, String senha, String telefone, LocalDate dtNascimento, LocalDate dtCadastro) {
        this.idUsuario = idUsuario;
        this.nomeCompleto = nomeCompleto;
        this.genero = genero;
        this.email = email;
        this.senha = senha;
        this.dtNascimento = dtNascimento;
        this.telefone = telefone;
        this.dtCadastro = dtCadastro;
    }

    public Usuario(String nomeCompleto, String genero, String email, String senha, String telefone, LocalDate dtNascimento, LocalDate dtCadastro) {
        this.nomeCompleto = nomeCompleto;
        this.genero = genero;
        this.email = email;
        this.senha = senha;
        this.dtNascimento = dtNascimento;
        this.telefone = telefone;
        this.dtCadastro = dtCadastro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    @Override
    public String toString() {
        return "Id Usuário: " + getIdUsuario() + "\n" +
                "Nome Completo: " + getNomeCompleto() + "\n" +
                "Gênero: " + getGenero() + "\n" +
                "Email: " + getEmail() + "\n" +
                "Senha: " + getSenha() + "\n" +
                "Data Nascimento: " + getDtNascimento() + "\n" +
                "Data Cadastro: " + getDtCadastro() + "\n";
    }
}