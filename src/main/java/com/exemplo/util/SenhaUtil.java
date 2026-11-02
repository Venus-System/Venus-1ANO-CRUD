package com.exemplo.util;

import org.mindrot.jbcrypt.BCrypt;

// Classe que cuida das senhas com BCrypt, para o banco nunca guardar a senha pura.
public class SenhaUtil {

    //aqui gera o hash. (é o resultado de passar um dado por uma função matemática que o transforma em um texto embaralhado).
    public static String gerarHash(String senhaPura){
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt(12));
    }

    // Construtor privado: a classe só tem métodos static, então não precisa criar objeto.
    private SenhaUtil(){}

    //aqui vai comparar a senha digitada com o hash (guardado no banco).
    public static boolean conferir(String senhaPura, String hash){
        if (senhaPura == null  || hash == null){
            return false;
        }
        try {
            return BCrypt.checkpw(senhaPura, hash);
        }catch (IllegalArgumentException e){
            return false;
        }
    }

}
