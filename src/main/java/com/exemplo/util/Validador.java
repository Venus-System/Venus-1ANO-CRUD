package com.exemplo.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validador {

    //padrão

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private static final Pattern TELEFONE = Pattern.compile("^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$");

    private static final Pattern SENHA = Pattern.compile("^.{6,}$");

    private Validador() {
    }

    public static boolean emailValido(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }

    public static boolean telefoneValido(String telefone) {
        return telefone == null || telefone.isBlank() || TELEFONE.matcher(telefone).matches();
    }

    public static boolean senhaValida(String senha) {
        return senha != null && SENHA.matcher(senha).matches();

    }

    public static boolean idadeMinimaValida(LocalDate nascimento, int anos) {
        return nascimento != null && !nascimento.isAfter(LocalDate.now().minusYears(anos));
    }

    // começa com letra ou número; depois letras, números, espaço e  ' ( ) / . , & + % -   (2 a 100 caracteres)
    private static final Pattern NOME_PRODUTO = Pattern.compile("^[\\p{L}\\d][\\p{L}\\d '()/.,&+%-]{1,59}$");
    //o pattern é uma classe que guarda o regex já compilado. (o modelo de texto que eu quiser aceitar)
    public static boolean nomeProdutoValido(String texto) {
        return texto != null && NOME_PRODUTO.matcher(texto).matches();
        //matcher é usado para validar.
        //matches devolve true se o texto inteiro seguir o padrão.
    }

    public static boolean pontuacaoValida(int pontuacao) {
        return pontuacao >= 0 && pontuacao <= 100;
    }

    private static final Pattern NOME_ALERGIA =
            Pattern.compile("^[\\p{L}\\p{N}][\\p{L}\\p{N} '()/.,&+%\\-]{1,99}$");

    public static boolean nomeAlergiaValido(String nome) {
        return nome != null && NOME_ALERGIA.matcher(nome).matches();
    }

}

