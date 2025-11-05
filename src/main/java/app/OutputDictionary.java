package app;

import java.util.HashMap;
import java.util.Map;

public class OutputDictionary {

    private static final Map<Integer, String> outputs = new HashMap<>();

    static {
        outputs.put(Linguagem20252Constants.NUMBER, "numero inteiro");
        outputs.put(Linguagem20252Constants.REAL, "numero real");
        outputs.put(Linguagem20252Constants.TEXT_DOUBLE, "texto");
        outputs.put(Linguagem20252Constants.TEXT_SINGLE, "texto");
        outputs.put(Linguagem20252Constants.TRUE, "constante logica");
        outputs.put(Linguagem20252Constants.FALSE, "constante logica");

        // SIMBOLOS ESPECIAIS
        outputs.put(Linguagem20252Constants.ASSIGN, "=");
        outputs.put(Linguagem20252Constants.EQUAL, "==");
        outputs.put(Linguagem20252Constants.NOT_EQUAL, "!=");
        outputs.put(Linguagem20252Constants.SEMICOLON, ";");
        outputs.put(Linguagem20252Constants.COLON, ":");
        outputs.put(Linguagem20252Constants.COMMA, ",");
        outputs.put(Linguagem20252Constants.DOT, ".");
        outputs.put(Linguagem20252Constants.LBRACE, "{");
        outputs.put(Linguagem20252Constants.RBRACE, "}");
        outputs.put(Linguagem20252Constants.LBRACKET, "[");
        outputs.put(Linguagem20252Constants.RBRACKET, "]");
        outputs.put(Linguagem20252Constants.LPAREN, "(");
        outputs.put(Linguagem20252Constants.RPAREN, ")");
        outputs.put(Linguagem20252Constants.PLUS, "+");
        outputs.put(Linguagem20252Constants.MINUS, "-");
        outputs.put(Linguagem20252Constants.STAR, "*");
        outputs.put(Linguagem20252Constants.SLASH, "/");
        outputs.put(Linguagem20252Constants.PERCENT, "%");
        outputs.put(Linguagem20252Constants.POWER, "**");
        outputs.put(Linguagem20252Constants.DOUBLE_PERCENT, "%%");
        outputs.put(Linguagem20252Constants.SHIFT_LEFT, "<<");
        outputs.put(Linguagem20252Constants.SHIFT_RIGHT, ">>");
        outputs.put(Linguagem20252Constants.SHIFT_LEFT_ASSIGN, "<<=");
        outputs.put(Linguagem20252Constants.SHIFT_RIGHT_ASSIGN, ">>=");
        outputs.put(Linguagem20252Constants.EXCLAMATION, "!");
        outputs.put(Linguagem20252Constants.PIPE, "|");
        outputs.put(Linguagem20252Constants.AMPERSAND, "&");

        // PALAVRAS RESERVADAS

        outputs.put(Linguagem20252Constants.BEGIN, "begin");
        outputs.put(Linguagem20252Constants.DEFINE, "define");
        outputs.put(Linguagem20252Constants.START, "start");
        outputs.put(Linguagem20252Constants.END, "end");
        outputs.put(Linguagem20252Constants.SET, "set");
        outputs.put(Linguagem20252Constants.READ, "read");
        outputs.put(Linguagem20252Constants.SHOW, "show");
        outputs.put(Linguagem20252Constants.IF, "if");
        outputs.put(Linguagem20252Constants.THEN, "then");
        outputs.put(Linguagem20252Constants.ELSE, "else");
        outputs.put(Linguagem20252Constants.LOOP, "loop");
        outputs.put(Linguagem20252Constants.WHILE, "while");
        outputs.put(Linguagem20252Constants.TYPE_FLAG, "flag");
        outputs.put(Linguagem20252Constants.TYPE_NUM, "num");
        outputs.put(Linguagem20252Constants.TYPE_REAL, "real");
        outputs.put(Linguagem20252Constants.TYPE_TEXT, "text");

        // IDENTIFICADOR

        outputs.put(Linguagem20252Constants.IDENTIFIER, "identificador");
    }


    public static String getCategory(int kind) {
        return outputs.getOrDefault(kind, "");
    }

}
