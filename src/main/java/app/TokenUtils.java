package app;

import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private static final Map<Integer, String> categories = new HashMap<>();

    static {
        // CONSTANTES
        categories.put(Linguagem20252Constants.NUMBER, "CONSTANTE NUMERICA INTEIRA");
        categories.put(Linguagem20252Constants.REAL, "CONSTANTE NUMERICA REAL");
        categories.put(Linguagem20252Constants.TEXT_DOUBLE, "CONSTANTE LITERAL DE TEXTO");
        categories.put(Linguagem20252Constants.TEXT_SINGLE, "CONSTANTE LITERAL DE TEXTO");
        categories.put(Linguagem20252Constants.TRUE, "CONSTANTE LOGICA");
        categories.put(Linguagem20252Constants.FALSE, "CONSTANTE LOGICA");

        // SIMBOLOS ESPECIAIS
        categories.put(Linguagem20252Constants.ASSIGN, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.EQUAL, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.NOT_EQUAL, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.SEMICOLON, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.COLON, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.COMMA, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.DOT, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.LBRACE, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.RBRACE, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.LBRACKET, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.RBRACKET, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.LPAREN, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.RPAREN, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.PLUS, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.MINUS, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.STAR, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.SLASH, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.PERCENT, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.POWER, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.DOUBLE_PERCENT, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.SHIFT_LEFT, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.SHIFT_RIGHT, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.SHIFT_LEFT_ASSIGN, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.SHIFT_RIGHT_ASSIGN, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.EXCLAMATION, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.PIPE, "SIMBOLO ESPECIAL");
        categories.put(Linguagem20252Constants.AMPERSAND, "SIMBOLO ESPECIAL");

        // PALAVRAS RESERVADAS

        categories.put(Linguagem20252Constants.BEGIN, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.DEFINE, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.START, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.END, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.SET, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.READ, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.SHOW, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.IF, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.THEN, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.ELSE, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.LOOP, "PALAVRA RESERVADA");
        categories.put(Linguagem20252Constants.WHILE, "PALAVRA RESERVADA");

        // TIPOS DE VARIAVEL

        categories.put(Linguagem20252Constants.TYPE_FLAG, "TIPO");
        categories.put(Linguagem20252Constants.TYPE_NUM, "TIPO");
        categories.put(Linguagem20252Constants.TYPE_REAL, "TIPO");
        categories.put(Linguagem20252Constants.TYPE_TEXT, "TIPO");

        // IDENTIFICADOR

        categories.put(Linguagem20252Constants.IDENTIFIER, "IDENTIFICADOR");
        
        // COMENTARIO
        
        categories.put(Linguagem20252Constants.BLOCK_COMMENT, "COMENTARIO_BLOCO");

        // ERROS

        categories.put(Linguagem20252Constants.INVALID_SIMBOL, "ERRO LÉXICO: símbolo inválido");
        categories.put(Linguagem20252Constants.UNCLOSED_STRING, "ERRO LÉXICO: constante literal não finalizada");
        categories.put(Linguagem20252Constants.UNCLOSED_QUOTE, "ERRO LÉXICO: constante literal não finalizada");
        categories.put(Linguagem20252Constants.MALFORMED_NUMBER, "ERRO LÉXICO: numero inteiro mal formado");
        categories.put(Linguagem20252Constants.MALFORMED_REAL, "ERRO LÉXICO: numero real mal formado");
        categories.put(Linguagem20252Constants.MALFORMED_IDENTIFIER, "ERRO LÉXICO: identificador mal formado");
        //categories.put(Linguagem20252Constants.UNCLOSED_BLOCK_COMMENT, "ERRO LÉXICO: comentario de bloco não fechado");


    }

    public static String getCategory(int kind) {
        return categories.getOrDefault(kind, " --- ");
    }

}
