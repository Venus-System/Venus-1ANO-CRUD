package com.exemplo.util;

import java.util.regex.Pattern;

public class Validador {

    //padrão

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private static final Pattern TELEFONE = Pattern.compile("^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$");

    private static final Pattern SENHA = Pattern.compile("^.{6,}$");

    private Validador(){}

        public static boolean emailValido(String email) {
            return email != null && EMAIL.matcher(email).matches();
        }

        public static boolean telefoneValido(String telefone){
            return telefone == null || telefone.isBlank() || TELEFONE.matcher(telefone).matches();
        }

        public static boolean senhaValida(String senha){
            return senha != null && SENHA.matcher(senha).matches();
        }
    }

