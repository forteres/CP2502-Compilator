package app;

import java.util.List;

public abstract class TokenStringBuilder {
    public static String formatTokenToString(List<Token> tokens) {
        StringBuilder formattedOutput = new StringBuilder();

        formattedOutput.append(String.format("%-12s %-7s %-7s %-50s %-7s\n",
                "Lexema", "Linha", "Coluna", "Categoria", "Código"));

        for (Token token : tokens) {
            if (token.kind == Linguagem20252Constants.EOF) {
                continue;
            }

            String categoria = TokenUtils.getCategory(token.kind);
            String codigo = (categoria.startsWith("ERRO LÉXICO")) ? "-" : String.valueOf(token.kind);

            formattedOutput.append(String.format("%-12s %-7d %-7d %-50s %-7s\n",
                    token.image,
                    token.beginLine,
                    token.beginColumn,
                    categoria,
                    codigo));
        }
        return formattedOutput.toString();
    }
}

