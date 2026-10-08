package com.exemplo.util;

import java.sql.Connection;

public class TesteConexao {
    public static void main(String[] args) {
        try (Connection conn = ConexaoBanco.conectar()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexão bem-sucedida!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar:");
            e.printStackTrace();
        }
    }
}