package com.exemplo.util;

import java.util.regex.Pattern;

public class Validador {

    private static final Pattern Email = Pattern.compile("^[\\\\w.+-]+@[\\\\w-]+(\\\\.[\\\\w-]+)+$");

    private static final Pattern Telefone = Pattern.compile("^\\\\(?\\\\d{2}\\\\)?\\\\s?9?\\\\d{4}-?\\\\d{4}$");

    private static final Pattern Senha= Pattern.compile("^.{5,}$");

    private Validador(){}
        public static boolean emailValido(String email) {
            return email != null && Email.matcher(email).matches();
        }

        public static boolean telefoneValido(String telefone){
            return telefone == null || telefone.isBlank() || Telefone.matcher(telefone).matches();
        }

        public static boolean senhaValida(String senha){
            return senha != null && Senha.matcher(senha).matches();
        }
    }

