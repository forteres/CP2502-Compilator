package app;

import java.util.List;

public abstract class TokenStringBuilder {
    public static String formatTokenToString(List<Token> tokens) {
        StringBuilder formattedOutput = new StringBuilder();

        int maxLexemaLength = tokens.stream()
                .filter(t -> t.kind != Linguagem20252Constants.EOF)
                .mapToInt(t -> t.image.length())
                .max()
                .orElse(12); // valor padrão

        int lexemaColumnWidth = maxLexemaLength + 2;
        String format = String.format("%%-%ds %%-%ds %%-%ds %%-%ds %%-%ds\n",
                lexemaColumnWidth, 7, 7, 50, 7);

        formattedOutput.append(String.format(format,
                "Lexema", "Linha", "Coluna", "Categoria", "Código"));

        for (Token token : tokens) {
            if (token.kind == Linguagem20252Constants.EOF) continue;

            String categoria = TokenUtils.getCategory(token.kind);
            String codigo = (categoria.startsWith("ERRO LÉXICO")) ? "-" : String.valueOf(token.kind);

            formattedOutput.append(String.format(format,
                    token.image,
                    token.beginLine,
                    token.beginColumn,
                    categoria,
                    codigo));
        }

        return formattedOutput.toString();
    }
}

