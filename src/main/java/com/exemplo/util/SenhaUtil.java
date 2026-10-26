package com.exemplo.util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {

    // Gera o hash BCrypt com salt aleatório e fator de custo 12.
    public static String gerarHash(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt(12));
    }

    private SenhaUtil() {
    }

    // Confere a senha com o hash salvo; valores nulos ou hashes inválidos retornam false.
    public static boolean conferir(String senhaPura, String hash) {
        if (senhaPura == null || hash == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(senhaPura, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
