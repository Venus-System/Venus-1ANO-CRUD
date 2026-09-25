package com.exemplo.model;

public class Analise {

    private int idAnalise;
    private String dtHrAnalise;
    private String resumoResultado;
    private int pontuacao;
    private int idUsuario;

    public Analise(int idAnalise, String dtHrAnalise, String resumoResultado, int pontuacao, int idUsuario) {
        this.idAnalise = idAnalise;
        this.dtHrAnalise = dtHrAnalise;
        this.resumoResultado = resumoResultado;
        this.pontuacao = pontuacao;
        this.idUsuario=idUsuario;

    }

    public int getIdAnalise() {
        return idAnalise;
    }

    public void setIdAnalise(int idAnalise) {
        this.idAnalise = idAnalise;
    }

    public String getDtHrAnalise() {
        return dtHrAnalise;
    }

    public void setDtHrAnalise(String dtHrAnalise) {
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

    @Override
    public String toString() {
        return "Id Análise: " + getIdAnalise()+"\n"+
                "Data e Hora Análise: " + getDtHrAnalise() + "\n"+
                "Resumo Resultado: " + getResumoResultado() + "\n"+
                "Pontuação: "+getPontuacao()+"\n"+
                "Id Usuário: "+getIdUsuario()+"\n";
    }
}
