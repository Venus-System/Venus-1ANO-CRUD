package com.exemplo.model;

import java.time.LocalDate;

public class UsuarioAlergia {

    private int idUsuarioAlergia;
    private LocalDate dtRegistro;
    private int grau;
    private int idUsuario;
    private int idAlergia;

    public UsuarioAlergia(int idUsuarioAlergia, LocalDate dtRegistro, int grau, int idUsuario, int idAlergia) {
        this.idUsuarioAlergia = idUsuarioAlergia;
        this.dtRegistro = dtRegistro;
        this.grau = grau;
        this.idUsuario = idUsuario;
        this.idAlergia = idAlergia;
    }

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

    @Override
    public String toString() {
        return "Id Usuário Alergia: " + getIdUsuarioAlergia() + "\n" +
                "Data Registro: " + getDtRegistro() + "\n" +
                "Grau: " + getGrau() + "\n" +
                "Id Usuário: " + getIdUsuario() + "\n" +
                "Id Alergia: " + getIdAlergia() + "\n";
    }
}
