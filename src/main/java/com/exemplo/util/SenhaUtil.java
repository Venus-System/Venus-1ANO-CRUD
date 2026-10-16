package com.exemplo.util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {

    //aqui gera o hash. (é o resultado de passar um dado por uma função matemática que o transforma em um texto embaralhado).
    public static String gerarHash(String senhaPura){
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt(12));
    }

    //aqui vai comparar a senha digitada com o hash (guardado no banco).
    public static boolean conferir(String senhaPura, String hash){
        return BCrypt.checkpw(senhaPura, hash);
    }

}
