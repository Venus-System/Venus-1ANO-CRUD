package com.exemplo.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validador {


    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private static final Pattern TELEFONE = Pattern.compile("^\\d{10,13}$");

    private static final Pattern NOME_USUARIO = Pattern.compile("^[\\p{L}][\\p{L} .'\\-]{1,99}$");

    private static final Pattern SENHA = Pattern.compile("^.{6,}$");

    private Validador() {
    }

    public static boolean emailValido(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }

    public static boolean telefoneValido(String telefone) {
        return telefone != null && TELEFONE.matcher(telefone).matches();
    }

    public static boolean nomeUsuarioValido(String nome) {
        return nome != null && NOME_USUARIO.matcher(nome.trim()).matches();
    }

    public static String somenteNumeros(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replaceAll("[^0-9]", "");
    }

    // O gênero é opcional; quando preenchido, deve corresponder a uma das opções aceitas.
    public static boolean generoValido(String genero) {
        return genero == null || genero.isBlank()
                || genero.equals("Feminino") || genero.equals("Masculino")
                || genero.equals("Outro") || genero.equals("Prefiro não informar");
    }

    public static boolean senhaValida(String senha) {
        return senha != null && SENHA.matcher(senha).matches();

    }

    public static boolean idadeMinimaValida(LocalDate nascimento, int anos) {
        return nascimento != null && !nascimento.isAfter(LocalDate.now().minusYears(anos));
    }

    public static boolean nascimentoDepoisDe1900(LocalDate nascimento) {
        return nascimento != null && nascimento.isAfter(LocalDate.of(1900, 1, 1));
    }

    // começa com letra ou número; depois letras, números, espaço e  ' ( ) / . , & + % -   (2 a 60 caracteres)
    private static final Pattern NOME_PRODUTO = Pattern.compile("^[\\p{L}\\d][\\p{L}\\d '()/.,&+%-]{1,59}$");

    public static boolean nomeProdutoValido(String texto) {
        return texto != null && NOME_PRODUTO.matcher(texto).matches();
    }

    public static boolean pontuacaoValida(int pontuacao) {
        return pontuacao >= 0 && pontuacao <= 100;
    }

    private static final Pattern NOME_ALERGIA =
            Pattern.compile("^[\\p{L}\\p{N}][\\p{L}\\p{N} '()/.,&+%\\-]{1,99}$");

    public static boolean nomeAlergiaValido(String nome) {
        return nome != null && NOME_ALERGIA.matcher(nome).matches();
    }

    public static boolean nivelPerigoValido(int nivel) {
        return nivel >= 1 && nivel <= 5;
    }

    public static boolean tipoIngredienteValido(String tipo) {
        if (tipo == null) {
            return false;
        }
        return tipo.matches("^[\\p{L}\\p{N}][\\p{L}\\p{N} .,'()/-]{1,99}$");
    }

    private static final Pattern NOME_INGREDIENTE =
            Pattern.compile("^[\\p{L}0-9][\\p{L}0-9 .,'()/%+\\-]{1,99}$");

    public static boolean nomeIngredienteValido(String nome) {
        return nome != null && NOME_INGREDIENTE.matcher(nome.trim()).matches();
    }

    private static final Pattern TEXTO_PREFERENCIA =
            Pattern.compile("^[\\p{L}0-9][\\p{L}0-9 .,;:'()/%$&+\\-]{0,254}$");

    // Preferências vazias são aceitas; textos preenchidos são validados sem espaços nas extremidades.
    public static boolean preferenciaOpcionalValida(String texto) {
        return preferenciaOpcionalValida(texto, 255);
    }

    public static boolean preferenciaOpcionalValida(String texto, int tamanhoMaximo) {
        return texto == null || (texto.length() <= tamanhoMaximo
                && (texto.isBlank() || TEXTO_PREFERENCIA.matcher(texto.trim()).matches()));
    }

    public static boolean tipoPeleValido(String tipo) {
        if (tipo == null) {
            return false;
        }
        String t = tipo.trim();
        return t.equals("Seca") || t.equals("Oleosa") || t.equals("Sensível")
                || t.equals("Mista") || t.equals("Normal");
    }

    public static boolean escalaValida(int valor) {
        return valor >= 1 && valor <= 10;
    }

    public static boolean curvaturaValida(int valor) {
        return valor >= 1 && valor <= 4;
    }

    public static boolean espessuraEOleosidadeValida(int valor) {
        return valor >= 1 && valor <= 10;
    }

    public static boolean resumoAnaliseValido(String resumo) {
        return resumo != null && !resumo.isBlank() && resumo.trim().length() <= 255;
    }

    public static boolean pontuacaoAnaliseValida(int valor) {
        return valor >= 0 && valor <= 100;
    }

    // Aceita identificadores positivos que cabem em um int, sem zero à esquerda.
    private static final Pattern ID = Pattern.compile("^[1-9]\\d{0,9}$");

    public static boolean idValido(String texto) {
        if (texto == null || !ID.matcher(texto.trim()).matches()) {
            return false;
        }
        try {
            return Integer.parseInt(texto.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

