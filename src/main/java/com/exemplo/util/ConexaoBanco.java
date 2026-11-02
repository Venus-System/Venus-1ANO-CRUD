package com.exemplo.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe que abre a conexão com o PostgreSQL usando as variáveis do .env.
public class ConexaoBanco {
    // Endereço, usuário e senha do banco. São lidos uma vez só, quando a classe é carregada.
    public static final String DB_URL;
    public static final String DB_USER;
    public static final String DB_PASSWORD;

    // Confere se o texto não veio ou veio vazio.
    private static boolean isNullOrEmpty(String valor){

        return valor == null  || valor.isEmpty();
    }

    // Bloco que roda uma vez: primeiro tenta as variáveis do sistema e, se não tiver, usa o arquivo .env.
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
            // Sem o driver não dá para conectar, então o erro é avisado na hora.
            throw new IllegalStateException("Driver do PostgreSQL não encontrado.", e);
        }

    }
    // Abre e devolve uma conexão nova. Os DAOs usam com try-with-resources, que fecha sozinho.
    public static Connection conectar() throws SQLException {
        if (isNullOrEmpty(DB_URL) || isNullOrEmpty(DB_USER) || isNullOrEmpty(DB_PASSWORD)){
            throw new SQLException("Configurações do banco ausentes");
        }
        return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
    }

    // Fecha a conexão, caso ela ainda esteja aberta.
    public static void desconectar(Connection cnn) throws SQLException{
        if (cnn != null && !cnn.isClosed()){
            cnn.close();
        }
    }

}
