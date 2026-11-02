package com.exemplo.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Model da tabela analise: cada objeto desta classe representa o resultado de uma análise feita para um usuário.
public class Analise {

    // Atributos: cada um guarda o valor de uma coluna da tabela.
    private int idAnalise;
    private LocalDateTime dtHrAnalise;
    private String resumoResultado;
    private int pontuacao;
    private int idUsuario;

    // Construtor completo: usado quando os dados são lidos do banco.
    public Analise(int idAnalise, LocalDateTime dtHrAnalise, String resumoResultado, int pontuacao, int idUsuario) {
        this.idAnalise = idAnalise;
        this.dtHrAnalise = dtHrAnalise;
        this.resumoResultado = resumoResultado;
        this.pontuacao = pontuacao;
        this.idUsuario=idUsuario;

    }

    // Construtor sem o id: usado no cadastro, porque o banco cria o id sozinho.
    public Analise(String resumoResultado, int pontuacao, int idUsuario) {
        this.resumoResultado = resumoResultado;
        this.pontuacao = pontuacao;
        this.idUsuario = idUsuario;
    }

    // Construtor com o id e os campos que podem mudar: usado na alteração.
    public Analise(int idAnalise, String resumoResultado, int pontuacao) {
        this.idAnalise = idAnalise;
        this.resumoResultado = resumoResultado;
        this.pontuacao = pontuacao;
    }

    // Getters e setters: leem e alteram os atributos, que são privados.
    public int getIdAnalise() {
        return idAnalise;
    }

    public void setIdAnalise(int idAnalise) {
        this.idAnalise = idAnalise;
    }

    public LocalDateTime getDtHrAnalise() {
        return dtHrAnalise;
    }

    public void setDtHrAnalise(LocalDateTime dtHrAnalise) {
        this.dtHrAnalise = dtHrAnalise;
    }

    public String getResumoResultado() {
        return resumoResultado;
    }

    public void setResumoResultado(String resumoResultado) {
        this.resumoResultado = resumoResultado;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDtHrAnaliseFormatada() {
        return dtHrAnalise == null ? "" : dtHrAnalise.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    // toString: mostra os dados do objeto em texto, útil para testes.
    @Override
    public String toString() {
        return "Id Análise: " + getIdAnalise()+"\n"+
                "Data e Hora Análise: " + getDtHrAnalise() + "\n"+
                "Resumo Resultado: " + getResumoResultado() + "\n"+
                "Pontuação: "+getPontuacao()+"\n"+
                "Id Usuário: "+getIdUsuario()+"\n";
    }
}
