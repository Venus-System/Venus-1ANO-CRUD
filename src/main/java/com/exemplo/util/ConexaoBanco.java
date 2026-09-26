package com.exemplo.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    public static final String DB_URL;
    public static final String DB_USER;
    public static final String DB_PASSWORD;

    private static boolean isNullOrEmpty(String valor){

        return valor == null  || valor.isEmpty();
    }

    static {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if(isNullOrEmpty(url)) {
            url = dotenv.get("DB_URL");
        }
        if(isNullOrEmpty(user)) {
            user = dotenv.get("DB_USER");
        }
        if(isNullOrEmpty(password)) {
            password = dotenv.get("DB_PASSWORD");
        }

        DB_URL = url;
        DB_USER = user;
        DB_PASSWORD = password;

        try {
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e);
        }

    }
    public static Connection conectar() throws SQLException {
        if (isNullOrEmpty(DB_URL) || isNullOrEmpty(DB_USER) || isNullOrEmpty(DB_PASSWORD)){
            throw new SQLException("Configurações do banco ausentes");
        }
        return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
    }

    public static void desconectar(Connection cnn) throws SQLException{
        if (cnn != null && !cnn.isClosed()){
            cnn.close();
        }
    }

}