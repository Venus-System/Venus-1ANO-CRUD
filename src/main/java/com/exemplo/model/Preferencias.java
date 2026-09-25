package com.exemplo.model;

public class Preferencias {

    private int idPreferencias;
    private String faixaPreco;
    private Boolean prefereVegano;
    private String restricoesDieta;
    private String categoriaPref;
    private String marcasFav;
    private int idUsuario;

    public Preferencias(int idPreferencias, String faixaPreco, Boolean prefereVegano, String restricoesDieta, String categoriaPref, String marcasFav, int idUsuario) {
        this.idPreferencias = idPreferencias;
        this.faixaPreco = faixaPreco;
        this.prefereVegano = prefereVegano;
        this.restricoesDieta = restricoesDieta;
        this.categoriaPref = categoriaPref;
        this.marcasFav = marcasFav;
        this.idUsuario = idUsuario;
    }

    public int getIdPreferencias() {
        return idPreferencias;
    }

    public void setIdPreferencias(int idPreferencias) {
        this.idPreferencias = idPreferencias;
    }

    public String getFaixaPreco() {
        return faixaPreco;
    }

    public void setFaixaPreco(String faixaPreco) {
        this.faixaPreco = faixaPreco;
    }

    public Boolean getPrefereVegano() {
        return prefereVegano;
    }

    public void setPrefereVegano(Boolean prefereVegano) {
        this.prefereVegano = prefereVegano;
    }

    public String getRestricoesDieta() {
        return restricoesDieta;
    }

    public void setRestricoesDieta(String restricoesDieta) {
        this.restricoesDieta = restricoesDieta;
    }

    public String getCategoriaPref() {
        return categoriaPref;
    }

    public void setCategoriaPref(String categoriaPref) {
        this.categoriaPref = categoriaPref;
    }

    public String getMarcasFav() {
        return marcasFav;
    }

    public void setMarcasFav(String marcasFav) {
        this.marcasFav = marcasFav;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Id Preferências: " + getIdPreferencias() + "\n" +
                "Faixa de Preço: " + getFaixaPreco() + "\n" +
                "Prefere Vegano: " + getPrefereVegano() + "\n" +
                "Restrições Diete: " + getRestricoesDieta() + "\n" +
                "Cateogira Favorita: "+getCategoriaPref()+"\n"+
                "Marca Favorita: "+getMarcasFav()+"\n "+
                "Id Usuário: " + getIdUsuario() + "\n";
    }
}
